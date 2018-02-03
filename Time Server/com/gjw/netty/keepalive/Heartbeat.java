package com.gjw.netty.keepalive;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class Heartbeat extends SimpleChannelInboundHandler<KeepAliveMessage>{
    
    //ʧ�ܼ�������δ�յ�client�˷��͵�ping����
    private int unRecPingTimes = 0 ;
    
    //ÿ��chanel��Ӧһ���̣߳��˴������洢��Ӧ��ÿ���̵߳�һЩ�������ݣ��˴���һ��ҪΪKeepAliveMessage����
    ThreadLocal<KeepAliveMessage> localMsgInfo = new ThreadLocal<KeepAliveMessage>(); 
    
    // ����ͻ���û���յ�����˵�pong��Ϣ��������
    private static final int MAX_UN_REC_PING_TIMES = 3;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, KeepAliveMessage msg) throws Exception {
        
        System.out.println(ctx.channel().remoteAddress() + " Say : sn=" + msg.getSn()+",reqcode="+msg.getReqCode());
        
        if(Utils.notEmpty(msg.getSn())&&msg.getReqCode()==1){
            msg.setReqCode(Constants.RET_CODE);
            ctx.channel().writeAndFlush(msg);
            // ʧ�ܼ���������
            unRecPingTimes = 0;
            if(localMsgInfo.get()==null){
                KeepAliveMessage localMsg = new KeepAliveMessage();
                localMsg.setSn(msg.getSn());
                localMsgInfo.set(localMsg);
            }
        }else{
            ctx.channel().close();
        }
        
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                /*����ʱ*/
                System.out.println("===�����===(READER_IDLE ����ʱ)");
                // ʧ�ܼ������������ڵ���3�ε�ʱ�򣬹ر����ӣ��ȴ�client����
                if(unRecPingTimes >= MAX_UN_REC_PING_TIMES){
                    System.out.println("===�����===(����ʱ���ر�chanel)");
                    // ��������N��δ�յ�client��ping��Ϣ����ô�رո�ͨ�����ȴ�client����
                    ctx.channel().close();
                }else{
                    // ʧ�ܼ�������1
                    unRecPingTimes++;
                }
            } else if (event.state() == IdleState.WRITER_IDLE) {
                /*д��ʱ*/   
                System.out.println("===�����===(WRITER_IDLE д��ʱ)");
            } else if (event.state() == IdleState.ALL_IDLE) {
                /*�ܳ�ʱ*/
                System.out.println("===�����===(ALL_IDLE �ܳ�ʱ)");
            }
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("����ԭ��"+cause.getMessage());
        if(localMsgInfo.get()!=null){
            /*
             * �ӹ��������Ƴ��豸�ŵ�Ψһ��ʾ����ʾ�豸����
             */
            // TODO
        }
        ctx.channel().close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        super.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // �رգ��ȴ�����
        ctx.close();
        if(localMsgInfo.get()!=null){
            /*
             * �ӹ��������Ƴ��豸�ŵ�Ψһ��ʾ����ʾ�豸����
             */
            // TODO
        }
        System.out.println("===�����===(�ͻ���ʧЧ)");
    }

}