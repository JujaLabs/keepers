package juja.microservices.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.AssertionFailedError;
import org.mockito.ArgumentMatcher;

import java.io.IOException;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

/**
 * @author Oleksii Petrokhalko.
 */
public final class TestUtils {
    private TestUtils() {
        //utility class
    }

    public static byte[] toJson(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(o);
    }


    public static class EqualMatcher<T> extends ArgumentMatcher<T> {
        private T thisObj;

        EqualMatcher(T thisObj) {
            this.thisObj = thisObj;
        }

        /**
         * Asserts that two objects are equal. Reflection is used to compare all fields of these values.
         * If they are not equal an AssertionFailedError is thrown.
         */
        @Override
        public boolean matches(Object argument) {
            try {
                assertReflectionEquals(thisObj, argument);
            } catch (AssertionFailedError e) {
                return false;
            }
            return true;
        }
    }

    public static <T> EqualMatcher<T> reflectionEqual(T entity) {
        return new EqualMatcher<T>(entity);
    }


}
