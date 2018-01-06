package kr.co.prot.app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.co.prot.app.consts.OauthSystems;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);
	
	@Value("#{propertyForOauth['oauth.naver.clientId']}")
	private String clientIdNaver;
	
	@Value("#{propertyForOauth['oauth.naver.clientSecret']}")
	private String clientSecretNaver;
	
	@Value("#{propertyForOauth['oauth.naver.callbackURL']}")
	private String callbackURLNaver;
	
	@Value("#{propertyForOauth['oauth.naver.tokenURL']}")
	private String tokenURLNaver;
	
	@RequestMapping(value="/login/{type}", method = RequestMethod.GET)
	public String onOAuthLogin(@PathVariable("type") String type, HttpServletRequest request) throws UnsupportedEncodingException {
		
		switch(type) {
		case OauthSystems.NAVER:

			String clientId = clientIdNaver;
			String redirectURI = URLEncoder.encode("http://www.prot.co.kr/oauth/callback/naver", "UTF-8");
			SecureRandom secureRandom = new SecureRandom();
			String state = new BigInteger(130, secureRandom).toString();
			String url = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
			
			url += "&client_id=" + clientId;
			url += "&redirect_uri=" + redirectURI;
			url += "&state=" + state;
			
			HttpSession session = request.getSession();
			session.setAttribute("state", state);
			
			return "redirect:" + url;
		}
		
		return null;
	}
	
	@RequestMapping(value="/callback/{system}", method=RequestMethod.GET)
	public String onOauthCallback(@PathVariable String system, HttpServletRequest request, Model model) throws IOException, JSONException {
		
		switch(system) {
		case OauthSystems.NAVER:
			
			String api = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
			api += "client_id=" + clientIdNaver;
			api += "&client_secret=" + clientSecretNaver;
			api += "&code=" + request.getParameter("code");
			api += "&state=" + request.getParameter("state");
			api += "&redirect_uri=" + URLEncoder.encode("http://www.prot.co.kr/oauth/token/naver", "UTF-8");
			
			URL url = new URL(api);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			
			int responseCode = httpURLConnection.getResponseCode();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseCode == 200?httpURLConnection.getInputStream():httpURLConnection.getErrorStream()));
			
			String line;
			StringBuffer stringBuffer = new StringBuffer();
			
			while((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
			
			bufferedReader.close();
			
			JSONObject jsonObject = new JSONObject(stringBuffer.toString());
			
			HttpSession session = request.getSession();
			
			session.setAttribute("access_token", jsonObject.getString("access_token"));
			session.setAttribute("token_type", jsonObject.getString("access_token"));
			session.setAttribute("refresh_token", jsonObject.getString("refresh_token"));
			session.setAttribute("expires_in", jsonObject.getString("expires_in"));
			
			api = "https://openapi.naver.com/v1/nid/me"; 
			
			String header = "Bearer " + jsonObject.getString("access_token");
			
			url = new URL(api);
			
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Authorization", header);
			
			responseCode = httpURLConnection.getResponseCode();
			
			bufferedReader = new BufferedReader(new InputStreamReader(responseCode == 200?httpURLConnection.getInputStream():httpURLConnection.getErrorStream()));
			
			line = "";
			stringBuffer = new StringBuffer();
			
			while((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
			
			jsonObject = new JSONObject(stringBuffer.toString());
			
			model.addAttribute("user", jsonObject.toString());
			
			return "token";
		}
		
		return null;
	}
	
}
