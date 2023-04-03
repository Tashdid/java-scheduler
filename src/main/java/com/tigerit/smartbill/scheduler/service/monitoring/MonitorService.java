////#region <DevTJ>
//package com.tigerit.smartbill.scheduler.service.monitoring;
//
//import java.util.concurrent.ThreadPoolExecutor;
//
//public class MonitorService implements Runnable{
//
//        private ThreadPoolExecutor executor;
//        private int seconds;
//        private boolean run=true;
//
//        public MonitorService(ThreadPoolExecutor executor, int delay)
//        {
//            this.executor = executor;
//            this.seconds = delay;
//        }
//        public void shutdown(){
//            this.run = false;
//        }
//        @Override
//        public void run()
//        {
//            while(run){
//                System.out.println(
//                        String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
//                                this.executor.getPoolSize(),
//                                this.executor.getCorePoolSize(),
//                                this.executor.getActiveCount(),
//                                this.executor.getCompletedTaskCount(),
//                                this.executor.getTaskCount(),
//                                this.executor.isShutdown(),
//                                this.executor.isTerminated()));
//                try {
//                    //todo:
//                        //note: !!!
//                    Thread.sleep(seconds*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }
//
////#endregion <DevTJ>
