package com.icia.wapoo.login;

import org.springframework.stereotype.Component;

@Component
public class RandomNumber {
	
	//난수만들기
	public String getPwd () 
	{
		String pwd= "";
		
		for (int i = 0; i < 6; i++) {
			
			
			if(i == 1 || i == 3 || i== 4)
			{
				pwd += (char) ((Math.random() * 26) + 97);
			}
			else {
				pwd += (int)(Math.random()*100);
			}
			
		}
		
		return pwd;
	}
	
	

	
}
