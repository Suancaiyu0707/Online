    RocketMq基于订阅发布机制，一个Topic拥有多个消息队列，一个Broker为每一个主题默认创建4个读队列和4个写队列。多个Broker组成一个集群，BrokerName由相同的多台Broker组成Naster-Slave架构，
brokerId为0代表Master，大于0表示slave，BrokerLiveInfozhong的lastUpdateTimestamp存储上次收到Broker心跳包的时间。
一、NameServer
    1、NameServer主要负责路由管理、服务注册和服务发现。为生产者和消费者提供关于主题Topic的路由信息。
    2、消息服务器Broker在启动的时候，会向NameServer中每个节点注册自己，并通过定时心跳保持自己和NameServer的连接。
    3、NameServer本身可以集群部署来保证NameServer的高可用，但是集群中的节点互相并不通信，这就可能会导致在某一刻，NameServer上的数据(比如broker列表)并不一致。这样是为了方便实现。
    4、RocketMQ路由注册是通过Broker和NameServer发送心跳来实现的，broker启动时会向所有NameServer发送心跳并注册自己。后面会每隔30s向集群中所有NameServer发送心跳信息来汇报自己的健康情况，
    这个健康信息是通过更新NameServer中brokerLiveTable的BrokerLiveInfo的lastUpdateTimestamp。同时NameServer本身会每隔10秒检查一下这个lastUpdateTimestamp，如果发现120s没有收到心跳包，
    则NameServer将会移除该Broker的路由信息，并关闭socket连接。
    5、NameServer有两个出发点来触发路由删除：
        a、NameServer会每隔10秒扫描brokerLiveTable,如果发现120秒还没有更新，则移除。
        b、Broker自身再正常被关系的时候，也会向NameServer执行unregisterBroker命令。
    6、RokcetMQ路由发现是非实时的，当Topic路由发生变化时，NameServer并不会主动推送给客户端，而是由客户端定时拉取主题最新的路由，根据主题名称拉取路由信息。
    配置:
        NettyServerConfig.useEpollNativeSelector: 是否开启epoll io模型，默认是false,linux环境下建议开启

二、Producer
    1、RocketMQ发送普通消息有三种实现方式：可靠同步发送、可靠异步发送、单向(oneway)发送。
        同步：发送者向MQ执行发送消息时，同步等待，直到消息服务器返回发送结果。
        异步：发送者向MQ执行发送消息时，指定消息发送成功后的回调函数。消息发送者线程不阻塞，直到运行借宿，回调函数会另启一个线程执行。
        单向：消息发送着向MQ执行发送消息时，直接返回，不等待消息服务器的结果，也不注册回调函数，所以也就不关心消息是否发送成功。
    2、批量消息发送：批量消息发送是将同一主题的多条消息一起打包发送到消息服务端，减少网络调用次数，提高网络传输效率。当然并不是说，批量消息越多越好，因为打包多条消息发送会影响其它线程发送消息的响应时间，并且单批次发送总长度也是有限制的：maxMessageSize
    3、每个消息的长度：
        4(总长度)+4(魔数)+4(CRC)+4(Flag)+4(body长度)+N(消息体)+2(属性长度)+N(拓展属性N字节) =22+N+N
Consumer
    1、消费者获取消息有两种方式：a、由消息服务器根据订阅信息，主动推送push给消费者。b、由消费者主动从消息服务器上拉取(pull)。

Topic:
    1、当消息生产者发送主题时，如果这个主题不存在，且BrokerConfig的autoCreateTopicEnable为true时，将返回MixAll.DEFAULT_TOPIC的路由信息。如果是false，则会抛出异常

Broker:
    1、一个Broker会关联多个FilterServer消息过滤服务器。
    2、负责消息的存储，分为持久化存储和不需要持久化的存储。
Message:
    flag：用来过滤的额外信息。
    keys：消息的索引，多个key可以用逗号分隔。


RocketMQ消息存储：首先我们要明白，rocketMQ是使用内存映射文件来提高I/O的吞吐量
    1、
        CommitLog：RocketMQ将所有主题的消息存储到同一个文件，这样是为了确保消息顺序写入文件，从而尽最大的能力的确保写消息的吞吐量。
            每一个文件默认为1G，一个文件写满后会再创建一个，以该文件中第一个偏移量为文件名，偏移量小于20位用0补齐。
        ConsumeQueue：由于消息中间件一般是基于消息主题的订阅能力，这样就会给按照主题检索消息带来了极大的不便。为了提高消息消费的效率，RocketMQ引入了ConsumeQueue消息队列文件，每个主题包含多个消息消费队列，每个消息消费队列都有一个消息文件。
            消息发到达CommitLog文件后，将异步转发到消息消费队列中，供消费者消费。
        IndexFile：设计的理念主要是为了加速消息的检索性能，根据消息的属性快速的从CommitLog文件中检索消息。主要存储key与offset的对应关系。
        事务状态服务：存储每条消息的事务状态。
        定时消息服务：每一个延迟级别对应一个消息消费队列，存储延迟队列的消息拉取进度。


