package util;

import android.text.TextUtils;

/**
 * Created by SharePDF developers
 */


/**
 * Clase auxiliar para dar soporte a otras clases.
 */
public class  Util  {
    private static String abecedario = "ABCDEFGHIJKLMNÑOPQRSTUVWXZ";
    private static String numeros = "0123456789";
    private static char  abec_mayus[] = abecedario.toCharArray();
   private static char abec_min[] = abecedario.toLowerCase().toCharArray();
    private static char num[] = numeros.toCharArray();

    /**
     *Método para la verificación de formato para un email.
     * @param email : email a verificar.
     * @return true o false según sea el caso.
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     *Método para la verificación de una contraseña.
     * @param password : contraseña a ser verificacda.
     * @return true o false según sea el caso.
     */
    public  static boolean isValidPassword(String password){
        return !TextUtils.isEmpty(password) && contiene_mayuscula_minuscula_numero(password)&&password.length()>6;
    }

    /**
     * Método para dar soporte a el método anterior.
     * @param password : contraseña a ser verificada.
     * @return true o false según sea el caso.
     */
    private  static boolean contiene_mayuscula_minuscula_numero(String password) {
        boolean contiene_may = false;
        boolean contiene_min = false;
        boolean contien_num = false;
        char contrasenya[]=password.toCharArray();
        for (int i = 0; i <password.length() ; i++) {
            for (int j = 0; j <abec_mayus.length ; j++) {
                if(contrasenya[i] == abec_mayus[j]){
                    contiene_may =true;
                    break;
                }
            }
            for (int j = 0; j <abec_min.length ; j++) {
                if(contrasenya[i] == abec_min[j]){
                    contiene_min = true;
                    break;
                }
            }
            for (int j = 0; j <num.length ; j++) {
                if(contrasenya[i] == num[j]){
                    contien_num = true;
                    break;
                }
            }
        }
        return  contiene_may && contiene_min && contien_num;
    }
}
