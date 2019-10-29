1、事件驱动和非事件驱动：
    非事件驱动：非事件驱动程序，在等待某个条件触发时，会不断的检查这个条件，直到条件满足，这种非常浪费CPU时间的。
    事件驱动：有机会释放cpu进入睡眠状态(注意这里是有机会，当然程序可自行决定不释放cpu),当事件触发时被操作系统唤醒，这样可以更加有效的使用cpu。一个典型的事件驱动的程序，就是一个死循环，并以一个线程的形式存在，这个死循环包括两个部分，第一个部分是按照一定的条件接收并选择一个要处理的事件，第二个部分就是事件的处理过程。程序的执行过程就是选择事件和处理事件，而当没有任何事件触发时，程序会因查询事件队列失败而进入睡眠状态，从而释放cpu。
        事件驱动的程序，必定会直接或者间接拥有一个事件队列，用于存储未能及时处理的事件。
        事件驱动的程序的行为，完全受外部输入的事件控制，所以，事件驱动的系统中，存在大量这种程序，并以事件作为主要的通信方式。
        事件驱动的程序，还有一个最大的好处，就是可以按照一定的顺序处理队列中的事件，而这个顺序则是由事件的触发顺序决定的，这一特性往往被用于保证某些过程的原子化。
2、epoll、poll、select
2、同步、异步、阻塞和非阻塞
    老张爱喝茶，废话不说，煮开水。
    出场人物：老张，水壶两把（普通水壶，简称水壶；会响的水壶，简称响水壶）。
    1) 老张把水壶放到火上，立等水开。（同步阻塞）
        老张觉得自己有点傻
    2) 老张把水壶放到火上，去客厅看电视，时不时去厨房看看水开没有。（同步非阻塞）
        老张还是觉得自己有点傻，于是变高端了，买了把会响笛的那种水壶。水开之后，能大声发出嘀~~~~的噪音。
    3) 老张把响水壶放到火上，立等水开。（异步阻塞）
        老张觉得这样傻等意义不大
    4) 老张把响水壶放到火上，去客厅看电视，水壶响之前不再去看它了，响了再去拿壶。（异步非阻塞）
        老张觉得自己聪明了

    总结：
        所谓同步异步，只是对于水壶而言。
        普通水壶，同步；响水壶，异步。
        虽然都能干活，但响水壶可以在自己完工之后，提示老张水开了。这是普通水壶所不能及的。
        同步只能让调用者去轮询自己（情况2中），造成老张效率的低下。

        所谓阻塞非阻塞，仅仅对于老张而言。
        立等的老张，阻塞；看电视的老张，非阻塞。
        情况1和情况3中老张就是阻塞的，媳妇喊他都不知道。虽然3中响水壶是异步的，可对于立等的老张没有太大的意义。所以一般异步是配合非阻塞使用的，这样才能发挥异步的效用。

3、netty的核心组件
    channel：代表一个到实体的开放连接，这个实体可以是文件或者网络套接字。netty里，可以把它看作是一个传入或传出数据的载体，它可以被关闭或者打开，连接或者断开连接。
        如：ServerSocketChannle、SocketChannel
    回调：类似异步通知。它其实是一个方法，一个指向已经被提供给另一个方法的方法的引用。这样另一个方法在合适的时间点，通过该引用回调该方法。netty在内部使用了回调来处理事件。
        当一个回调被触发时，相关的事件可以被一个interfaceChannelHandler的实现处理。比如自定义的OnlineServerHandler
    Future：提供了另一种在操作完成后通知应用程序的方式。这个对象可以看作是一个异步操作的结果的占位符；它将在未来的未来的某个时刻完成。可以消除手动检查某种操作是否完成。
        ChannelFuture future = b.bind(port).sync();
        future.addListener(new ChannelFutureListener(){
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("bind success");
            }
        });
        注意：每个Netty的出站I/O操作都将返回一个ChannelFuture；也就是说它们都不会阻塞。
    事件和ChannelHandler：netty使用不同的事件来通知我们状态的改变或者是操作的状态，然后并交给相应的ChannelHandler进行处理。

4、netty的异步编程模型基于java nio的异步和事件驱动实现的，也是建立在Future和回调的概念之上的，而将事件派发到ChannelHandler的方法则发生在更深的层次。
    netty通过触发事件将Selector从应用程序中抽象出来，消除了本来要写的派发代码，在内部，将会为每个channle分配一个EventLoop用以处理所有的事件。EventLoop本身只由一个线程驱动，它负责处理一个channel的所有I/O事件，并且在该EventLoop的整个生命周期中都不会改变。这样就可以不用担心并发同步的问题了。

5、Netty的组件和设计
    Channel接口
        基本的I/O操作(bind、connect、read和write)依赖于底层网络传输所提供的原语。Channel所提供的接口Api，大大降低了直接使用Socket类的复杂性。

    EventLoop接口
        一个EventLoopGroup包含一个或多个EventLoop；
        一个EventLoop在它的生命周期内只和一个Thread绑定；
        所有EventLoop处理的I/O事件都将在它专有的Thread上被处理。
        一个channel在它的生命周期内只注册一个EventLoop，所以一个给定的channel的I/O操作都是由相同的Thread来执行，这也消除了对于同步的需要。
        一个EventLoop可能被分配给一个或多个Channel。

    ChannelFuture接口：
        Netty中所有的I/O操作都是异步的。所以我们需要一种用于在之后的某个时间点确定其结果的方法，为此，netty提供了ChannelFuture接口，其addListener方法注册了一个ChannelFutureListener，以便在某个操作完成时(无乱是否成功)得到通知。
        可以将ChannelFuture看作是将来要操作的结果的占位符，所有属于同一个Channel的操作都会被保证其将于它们被调用的顺序执行。
6、ChannelHandler接口
    ChannelHandler充当了所有处理入站和出站数据的应用程序的逻辑的容器。ChannelHandler的方法是由网络事件触发的。
7、ChannelPipeline 提供了ChannelHandler的容器，当一个channel被创建时，它会被自动的分配到它专属的ChannelPipeline。
    ChannelHandler在ChannelPipeline中的执行顺序是由它们被添加的顺序所决定的。
    ChannelHandler和ChannelPipeline之间的绑定是通过 ChannelHandlerContext 来绑定的。
    不管是出站ChannelHandler还是ChannelHandler，对于同一个channel,它会绑定到同一个ChannelPipeline上，ChannelPipeline自己能区分二者。
8、在netty中，有两种发送消息的方式，你可以直接写到Channel中，也可以写到和channelHandler相关联的ChannelHandlerContext中，前者从ChannelPipeline尾端开始流动，而后者将导致消息从ChannelPipeline中的下一个ChannelHandler开始流动。

9、Netty的内置传输
    NIO：非阻塞I/O，基于JDK1.4的API。选择器背后的基本概念是充当一个注册表。选择器运行在一个检查注册表中channel的状态变化并对这些变化作出相应响应的线程上，在应用程序对状态的改变作出响应后，选择器将会被重置，并将重复这个过程。
    epoll：是一种专门为linux系统提供的传输协议，比传统的NIO更高效。只需要将NioEventLoopGroup替换为EpollEventLoopGroup,并将NioServerSocketChannel.class替换为EpollServerSocketChannel.class
    OIO：它是同步的，可以用来实现一些迁移。
10、ByteBuf
    它是netty自定义的一个ByteBuf，高效易用，提供了一系列优点：
        可以被用户自定义的缓冲区类型拓展。
        通过内置的复合缓冲区类型实现了透明的另拷贝。
        流量可以按需增长。
        在读和写两种模式之间切换，不需要调用ByteBuffer的flip方法。
        读和写使用了不同的索引，分别用来记录读取的字节数和写入的字节数。
        支持方法的链式调用。
        支持引用计数。
        支持池化。

    堆缓冲区：最常用的ByteBuf模式是将数据存储在JVM的堆空间中。这种模式被也被称为支撑数组，它能够在没有使用池化的情况下提供快速的分配和释放。
        ByteBuf heapBuf = Unpooled.buffer();//创建一个堆缓冲区
    直接缓冲区：允许JVM实现通过本地调用来分配内存。这样可以避免每次操作I/O操作之前(或者之后)将缓冲区的内容复制到一个中间缓冲区(或者从中间缓冲区把内容复制到用户缓冲区)
         ByteBuf directBuf = Unpooled.directBuffer();
         直接缓冲区的内容将驻留在常规的会被垃圾回收的堆之外。在网络传输的时候，这样就可以避免堆缓冲区到直接缓冲区的复制。
         直接缓冲区的缺点是，相对于堆缓冲区，它们的分配和释放都比较昂贵。
    复合缓冲区：可以同时包含堆内存缓冲和直接内存缓冲。它提供了一个将多个缓冲区表示为单个合并缓冲区的虚拟表示。
         CompositeByteBuf compositeBuf = Unpooled.compositeBuffer();//创建一个复合缓冲区

11、一个 EventLoop 将由一个永远都不会改变的Thread驱动，单个EventLoop可能会被指派用于服务多个channel。EventLoop有一个方法parent()，用户返回EventLoopGroup的引用。
    由I/O触发的事件会流经ChannelHandler的ChannelPipeLine。所有的I/O操作和事件都由已经分配给了EventLoop的那个Thread来处理。
零拷贝：
    零拷贝只有在使用NIO和Epoll传输时才能使用的特性。它使你可以更高效的将数据从文件系统拷贝到网络接口，而不需要将其从内核复制到用户空间，从而提高性能。但是并不是所有的系统都支持这一特性。


12、NioEventLoop：会启动两种类型下线程，主要做了两件事
    a、一种线程是用来监听客户端的连接
    b、一种线程是用来处理每个连接的读写
    12.1、NioEventLoop的创建
        new NioEventLoopGroup[线程组，默认创建2*cpu]
            new ThreadPerTaskExecutor(this.newDefaultThreadFactory())[线程创建器]
            for(){newChild()}[创建NioEventLoop]//一个NioEventLoop跟一个selector绑定
            this.chooser = chooserFactory.newChooser()[线程选择器，给每个新连接分配NioEventLoop线程]
13、netty服务端启动
    1）创建服务端channel:
        bind()-->AbstractBootstrap.doBind()-->AbstractBootstrap.initAndRegister()：初始化并注册-->ChannelFactory.newChannel()

    2）初始化channel
    3）注册selector:AbstractBootstrap.initAndRegister()->config().group().register(channel)-
        this.eventLoop=eventLoop(绑定线程)
        register0(实际注册)
            doRegister(调用jdk底层注册，将jdk的一个channel注册到一个selector上)
            invokeHandlerAddedIfNeeded(处理一些channelHandler的回调)

14、ChannelInboundHandler方法
    ChannelUnregistered：  Channel 已经被创建，但还未注册到 EventLoop并且无法处理处理 I/O 时被调用
    ChannelRegistered：  Channel 已经被注册到了 EventLoop，并且能够处理 I/O 时被调用
    ChannelActive：  当 Channel 处于活动状态时被调用;Channel 已经连接/绑定并且已经就绪。它现在可以接收和发送数据了
    ChannelInactive： 当 Channel 离开活动状态并且不再连接它的远程节点时被调用
    channelReadComplete：  当Channel上的一个读操作完成时被调用
    channelRead： 当从 Channel 读取数据时被调用


15、ChannelOutboundHandler方法，注意，ChannelPromise是channerFuture的一个子类
    bind(ChannelHandlerContext,SocketAddress,ChannelPromise)：当请求将 Channel 绑定到本地地址时被调用
    connect(ChannelHandlerContext, SocketAddress,SocketAddress,ChannelPromise)：当请求将 Channel 连接到远程节点时被调用
    disconnect(ChannelHandlerContext,ChannelPromise)：当请求将 Channel 从远程节点断开时被调用
    close(ChannelHandlerContext,ChannelPromise)：当请求关闭 Channel 时被调用
    deregister(ChannelHandlerContext,ChannelPromise)：当请求将 Channel 从它的 EventLoop 注销 时被调用
    read(ChannelHandlerContext)：当请求从 Channel 读取更多的数据时被调用
    flush(ChannelHandlerContext)：当请求通过 Channel 将入队数据冲刷到远程节点时被调用
    write(ChannelHandlerContext,Object,ChannelPromise)：当请求通过 Channel 将数据写到远程节点时 被调用

16、每一个新创建的 Channel 都将会被分配一个新的 ChannelPipeline。这项关联是永久性 的;Channel 既不能附加另外一个 ChannelPipeline，也不能分离其当前的。
    channelHandler可以通过添加、删除或者替换其他的 ChannelHandler 来实时地修改 ChannelPipeline 的布局。

17、ChannelPipeline方法
    fireChannelRegistered：调用 ChannelPipeline 中下一个 ChannelInboundHandler 的 channelRegistered(ChannelHandlerContext)方法
    fireChannelUnregistered：调用 ChannelPipeline 中下一个 ChannelInboundHandler 的 channelUnregistered(ChannelHandlerContext)方法
    fireChannelActive： 调用 ChannelPipeline 中下一个 ChannelInboundHandler 的 channelActive(ChannelHandlerContext)方法
    fireChannelInactive：调用 ChannelPipeline 中下一个 ChannelInboundHandler 的 channelInactive(ChannelHandlerContext)方法
    fireExceptionCaught：调用 ChannelPipeline 中下一个 ChannelInboundHandler 的 exceptionCaught(ChannelHandlerContext, Throwable)方法
    fireUserEventTriggered：调用 ChannelPipeline 中下一个 ChannelInboundHandler 的 userEventTriggered(ChannelHandlerContext, Object)方法
    fireChannelRead：调用 ChannelPipeline 中下一个 ChannelInboundHandler 的 channelRead(ChannelHandlerContext, Object msg)方法
    fireChannelReadComplete：调用 ChannelPipeline 中下一个 ChannelInboundHandler 的 channelReadComplete(ChannelHandlerContext)方法
   fireChannelWritabilityChanged：调用 ChannelPipeline 中下一个 ChannelInboundHandler的channelWritabilityChanged(ChannelHandlerContext)方法
18、ChannelHandlerContext
    ChannelHandlerContext 和 ChannelHandler 之间的关联(绑定)是永远不会改变的，所以缓存对它的引用是安全的;
    相对于其他类的同名方法，ChannelHandler Context的方法将产生更短的事件流，应该尽可能地利用这个特性来获得最大的性能。
    ChannelHandlerContext 有很多的方法，其中一些方法也存在于 Channel 和 ChannelPipeline 本身上，但是有一点重要的不同。如果调用 Channel 或者 ChannelPipeline 上的这 些方法，它们将沿着整个 ChannelPipeline 进行传播。而调用位于 ChannelHandlerContext 上的相同方法，则将从当前所关联的 ChannelHandler 开始，并且只会传播给位于该 ChannelPipeline 中的下一个能够处理该事件的 ChannelHandler。