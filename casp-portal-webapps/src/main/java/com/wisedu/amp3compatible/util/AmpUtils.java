package com.wisedu.amp3compatible.util;

import com.wisedu.amp3compatible.model.IdentityToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.interfaces.RSAPublicKey;

public abstract class AmpUtils {
	
	private static RSAPublicKey PublicKey = null;
	
	static {
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(AmpUtils.class.getClassLoader().getResourceAsStream("key/PublicKey"));
			PublicKey = (RSAPublicKey) inputStream.readObject();
			//Objects.requireNonNull(PublicKey);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/**
	 * 获得当前用户进入应用的身份令牌信息
	 * 
	 * @param request
	 * @return
	 */
	public static IdentityToken getIdentityToken(HttpServletRequest request) throws AmpCurrentGroupFormatException {
		//Objects.requireNonNull(request);
		String identityTokenString = request.getParameter("gid_");
		if (identityTokenString == null || identityTokenString.isEmpty()) {
			throw new AmpCurrentGroupFormatException("身份信息为空");
		}
		try {
			identityTokenString = decodeIdentityToken(identityTokenString);
			return constructInentityToken(identityTokenString);
		} catch (Exception e) {
			throw new AmpCurrentGroupFormatException(e);
		}
	}

	private static IdentityToken constructInentityToken(String identityTokenString) {
		String[] tokens = identityTokenString.split(":");
		if (tokens == null || tokens.length != 2) {
			throw new RuntimeException("非法的身份信息");
		}
		return new IdentityToken(tokens[0], tokens[1]);
	}

	public static String decodeIdentityToken(String identityString) {
		try {
			identityString = StringUtils.newStringUtf8(Base64.decodeBase64(identityString));
			return RSAUtil.decryptByPublicKey(identityString, PublicKey);
		} catch (Exception e) {
			throw new RuntimeException("非法的身份信息",e);
		}
	}

	/**
	 * 从加密后的字符串获取当前登录用户的当前用户组
	 * @return 如果url中没有gid_参数，则返回null
	 * @throws AmpCurrentGroupFormatException
	 *
	 */
	@Deprecated
	public static String getCurrentGroupId(HttpServletRequest request) throws AmpCurrentGroupFormatException{
		if(request == null){
			throw new RuntimeException("request为空");
		}
		String codeStr = request.getParameter("gid_");
		if(codeStr == null || "".equals(codeStr)){
			return null;
		}
		String decodeString = decodeStr(codeStr);
		if(decodeString.startsWith("AMP")){
			String roleId = decodeString.substring(3);
			return roleId;
		}else{
			throw new AmpCurrentGroupFormatException();
		}
	}
	
	/**
	 * 解密字符串
	 * @param pwd
	 * @return
	 */
	private static String decodeStr(String pwd){  
        byte[] debytes = Base64.decodeBase64(new String(pwd).getBytes());  
        return new String(debytes);  
    }
	

}
