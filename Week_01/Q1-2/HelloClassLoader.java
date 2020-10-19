import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class HelloClassLoader extends ClassLoader{

    public static void main(String[] args)  {
        HelloClassLoader helloClassLoader = new HelloClassLoader();
        try {
            Class helloClass = helloClassLoader.loadClass("Hello");
            Object helloObj = helloClass.newInstance();
            Method helloMethod = helloClass.getMethod("hello");
            helloMethod.invoke(helloObj);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            //拼接文件名
            String fileName = name + ".xlass";
            //读取文件输入流
            InputStream in = getClass().getResourceAsStream(fileName);
            if (in == null) {
                return super.loadClass(name);
            }
            byte[] b = new byte[in.available()];
            in.read(b);
            //解码
            decode(b);

            return defineClass(name, b, 0, b.length);
        } catch (IOException ex) {
            throw new ClassNotFoundException(name);
        }
    }

    /**
     * 解码
     * @param data
     */
    private  void decode(byte[] data) {
        for(int i=0;i< data.length;i++) {
            data[i] = (byte)(255 - data[i]);
        }
    }
}
