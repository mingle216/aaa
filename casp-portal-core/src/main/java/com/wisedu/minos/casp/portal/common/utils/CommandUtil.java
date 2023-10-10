package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.service.impl.CommonApiAdapterImpl;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.logging.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：Java执行Linux命令操作类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title CommandUtil
 * @Author: jcx
 * @Date: 2020/9/25
 */
public class CommandUtil {


    private static final Logger logger = LogManager.getLogger(CommandUtil.class);

    //private static final String LINE = "\r\n";

    private CommandUtil() {
        throw new AssertionError();
    }

    /**
     * 执行shell命令，并返回对应的结果
     * @param command
     */
//    public static void execCommand(String command, DeployInstanceProcessBean resultInfo) {
//        LOGGER.info("开始执行:" + command);
//        execCommand(new String[]{command}, resultInfo);
//    }
//
//    public static void execCommand(String[] commands,DeployInstanceProcessBean resultInfo) {
//        if (commands == null || commands.length == 0) {
//            throw new MinosException("传入的命令为空");
//        }
//        List<String> cmds = new ArrayList<>();
//        cmds.add("/bin/sh");
//        cmds.add("-c");
//        for (String cmd : commands){
//            cmds.add(cmd);
//        }
//        Process process = null;
//        BufferedReader br = null;
//
//        DataOutputStream os = null;
//        try {
//            process = Runtime.getRuntime().exec(cmds.toArray(new String[cmds.size()]), null);
//
//            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            String inline = null;
//            while ((inline = br.readLine()) != null) {
//                resultInfo.getLogDetail().append(inline);
//                resultInfo.getLogDetail().append(LINE);
//            }
//            br.close();
//            br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            while ((inline = br.readLine()) != null) {
//                resultInfo.getLogDetail().append(inline);
//                resultInfo.getLogDetail().append(LINE);
//            }
//        } catch (Exception e) {
//            LOGGER.error("执行"+commands[0]+"失败",e);
//        } finally {
//            resultInfo.setEndDeployTimeStamp(System.currentTimeMillis());
//            try {
//                if (os != null) {
//                    os.close();
//                }
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException e) {
//            }
//            if (process != null) {
//                process.destroy();
//            }
//        }
//    }

    public static CommandResult execCommand(String command) {
        return execCommand(new String[]{command}, true);
    }

    public static CommandResult execCommand(String[] commands) {
        return execCommand(commands, true);
    }

    public static CommandResult execCommand(String[] commands, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }

        List<String> cmds = new ArrayList<>();
        cmds.add("/bin/sh");
        cmds.add("-c");
        for (String cmd : commands){
            cmds.add(cmd);
        }

        Process process = null;
        InputStream successResult = null;
        InputStream errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(cmds.toArray(new String[cmds.size()]), null);

            result = process.waitFor();
            // get command result
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = process.getInputStream();
                errorResult = process.getErrorStream();

                byte[] buf = new byte[1024];
                for (int n; (n = successResult.read(buf)) != -1; ) {
                    successMsg.append(new String(buf, 0, n));
                }

                for (int n; (n = errorResult.read(buf)) != -1; ) {
                    errorMsg.append(new String(buf, 0, n));
                }
            }
        } catch (Exception e) {
            logger.info("执行"+commands[0]+"失败",e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                logger.info("context",e);
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null : errorMsg.toString());
    }

    /**
     * result of command
     * <ul>
     * <li>{@link CommandResult#result} means result of command, 0 means normal, else means error, same to excute in
     * linux shell</li>
     * <li>{@link CommandResult#successMsg} means success message of command result</li>
     * <li>{@link CommandResult#errorMsg} means error message of command result</li>
     * </ul>
     *
     * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-16
     */
    @SuppressWarnings("unused")
    public static class CommandResult {

        /**
         * result of command
         **/
        private int result;
        /**
         * success message of command result
         **/
        private String successMsg;
        /**
         * error message of command result
         **/
        private String errorMsg;

        public CommandResult(int result) {
            this.result = result;
        }

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("result = ").append(result);
            if (! StringUtils.isEmpty(errorMsg)){
                builder.append("Error = ").append(errorMsg);
            }
            if (!StringUtils.isEmpty(successMsg)){
                builder.append("Success = ").append(successMsg);
            }
            return builder.toString();
        }
    }
}
