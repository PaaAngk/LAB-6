package ISTB_19_2_Pervykh.menu;

public class Menu_control {

            public static boolean staffName (String name) {
            if (name.matches("^[\\p{L} ]+")) { //Содержание букв и пробел
                return true;
            }
            else {
                Menu_GUI.infoBox("<i>Неправильно введено имя</i>");
            }
            return false;
        }

        public static boolean staffAge (String age) {
            if (age.matches("-?\\d+(\\d+)?") ) {
                if (Integer.parseInt(age) > 0 && Integer.parseInt(age) < 150 ){return true;}
                else {Menu_GUI.infoBox("<i>Неправильно введен возраст</i><p>Возраст должен быть больше 0 и меньше 150.</p>");}
            }
            else {Menu_GUI.infoBox("<i>Неправильно введен возраст</i><p>Возраст должен быть больше 0 и меньше 150.</p>");}
            return false;
        }

}
