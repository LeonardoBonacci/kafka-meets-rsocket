package guru.bonacci.rsocket.tokafka.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import guru.bonacci.rsocket.tokafka.dto.Foo;

public class RandomUtils {

    public static Foo randomReplaceFirstChar(Foo foo){
    	foo.setBar(randomReplaceFirstChar(foo.getBar()));
    	return foo;
    }

    public static String randomReplaceFirstChar(String str){
    	return RandomStringUtils.randomAlphabetic(1) + str.substring(1);
    }

    public static Foo randomReplaceLastChar(Foo foo){
    	foo.setBar(randomReplaceLastChar(foo.getBar()));
    	return foo;
    }

    public static String randomReplaceLastChar(String str){
    	return StringUtils.chop(str) + RandomStringUtils.randomAlphabetic(1);
    }
}
