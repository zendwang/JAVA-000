//javac Hello.java
//javap -c Hello

public class Hello {
//  public void hello(){
//     System.out.println("Hello, classLoader!");
//  }
  public static void main(String[] args) {
      int a = 1;
      int b = 2;
      int c = a + b;
      int d = c - a;
      int e = c * b;
      int f = e / 2;

      if(d > 0) {
          for(int i=0;i < f; i++) {
              System.out.println(i);
          }
      }
  }
}