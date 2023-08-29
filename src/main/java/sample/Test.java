package sample;

public class Test {
    public static void main(String[] args) {
        int x=1;
        for(int i=1;i<=10;i++){
            System.out.println(i);
            if(i==5) x=i;
        }
        int a=x;
    }
}
