package com.online.leetcode;

/***
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，
 * 并且它们的每个节点只能存储 一位 数字。
 *
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 *
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 *
 * 示例：
 *
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class AddTwoNumbers {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2){
        ListNode cur = new ListNode(0);
        int carry=0;//用来表示进位
        ListNode pre = cur;//用来保存链表的头元素的饮用，因为后面的cur都会不断的变为下一个节点
        //遍历第一个和第二个参数
        while (l1!=null||l2!=null){
            //如果第一个参数不是空
            int x = l1==null?0:l1.val;
            int y = l2==null?0:l2.val;
            int sum = x+y+carry;
            carry = sum/10;//判断两数之和是否要进位
            sum = sum%10;//两数之和之后进位的余数
            cur.next=new ListNode(sum);//把新节点接入到next
            cur=cur.next;
            if(l1!=null) l1=l1.next;
            if(l2!=null) l2=l2.next;
        }
        if(carry>0){//可能遍历到最后一位还有进位
            cur.next=new ListNode(carry);
        }
        //去除头节点0
        return pre.next;
    }

    class ListNode {
          int val;
          ListNode next;
          ListNode(int x) { val = x; }
     }

    public static void main(String[] args) {

    }
}
