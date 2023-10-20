public class NumerePrime {
    public static void main(String[] args) {
        int a = 15;
        int b = 55;
        System.out.println(checkIfPrime(a, b));
    }

    private static boolean checkIfPrime(int a, int b) {
        if(a == b){
            return false;
        }
        if(a % b == 0 || b % a == 0){
            return false;
        }
        while(a != b){
            if(a > b) {
                a = a - b;
            }
            if(b > a) {
                b = b - a;
            }
        }
        if(a != 1){
            return false;
        }
        return true;
    }
}
