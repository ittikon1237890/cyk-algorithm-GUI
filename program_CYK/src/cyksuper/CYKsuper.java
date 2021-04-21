/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cyksuper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

class CYK {

    String word;
    String data[][];
    String data_Step[][];
    String data_position[][];
    String grammar[][];
    String Store_grammar[];
    String Text_grammar;
    String Store_convert[];
    int count = 0;
    int count_i = 0;
    int count_j = 0;
    int store_j = 0;
    int checkpoint_accept = 0;

    public CYK() {
    }

    public CYK(String word, String grammar) {
        this.word = word;
        this.Text_grammar = grammar;
        data = new String[word.length() + 1][word.length() + 1];
        data_Step = new String[word.length() + 1][word.length() + 1];
        data_position = new String[word.length() + 1][word.length() + 1];
        Store_convert = new String[word.length()];
        
        for (int i = 0; i < word.length() + 1; i++) {
            for (int j = 0; j < word.length() + 1; j++) {
                data_Step[i][j] = "";
            }
        }
        for (int i = 0; i < word.length() + 1; i++) {
            for (int j = 0; j < word.length() + 1; j++) {
                data_position[i][j] = "";
            }
        }
    }

    public String Ran_word(int ran, String garmmar) { //07600464
        /// หาอักขระที่ไม่ซ้ำกันจากGarmmar
        ArrayList<String> ran_word = new ArrayList<String>();
        ArrayList<String> ran_word2 = new ArrayList<String>();
        for (int j = 0; j < garmmar.length(); j++) { //เช็คเอาตัวอักษรตัวเล็กออกจากgarmmar
            if (Character.isLowerCase(garmmar.charAt(j))) {
                ran_word.add(String.valueOf(garmmar.charAt(j)));
            }
        }
        HashSet<String> hset = new HashSet<String>(ran_word); //ใช้HashSet เพื่อเก็บตัวอักขระที่ไม่ซ้ำกัน

        Iterator<String> it = hset.iterator();

        while (it.hasNext()) { //วนรอบHashSet เพื่อนำค่าจากHashSet ไปไว้อีก อาเรย์ลิชนึง
            String element = it.next();
            ran_word2.add(element);
        }
        int random = ran; // จำนวนความยาวตัวของword
        String ranWord = ""; //เก็บค่า word ที่สุ่มมา
        Random rand = new Random();
        for (int j = 0; j < random; j++) {
            int n = rand.nextInt(ran_word2.size()); //สุ่มตำแหน่งในอาเรย์ลิสที้เก็บอักขระไว้
            ranWord += ran_word2.get(n); //แล้วบวกต่อเพิ่มไปในสตริง
        }
        return ranWord;
    } //07600461

    public void Put_grammar() {//07600431
        Scanner scan = new Scanner(Text_grammar);
        for (int i = 0; i < Text_grammar.length(); i++) {
            if (Text_grammar.charAt(i) == '-') {
                count++;
                count_i++;
            }
        }
        Store_grammar = new String[count_i];
        int num = 0;
        for (int i = 0; i < count; i++) {
            String k = scan.nextLine();
            Store_grammar[num] = k;
            for (int j = 0; j < k.length(); j++) {
                if (k.charAt(j) == '|') {
                    store_j++;
                }
            }
            if (store_j > count_j) {
                count_j = store_j;
            }
            store_j = 0;
            num++;
        }
        this.grammar = new String[count_i][count_j + 2];
        Input_grammar();
    }  //07600431

    public void Input_grammar() { //07600431
        for (int i = 0; i < count_i; i++) {
            String str = Store_grammar[i];
            String[] arrOfStr = str.split("[ ->|]+");
            for (int j = 0; j < arrOfStr.length; j++) {
                grammar[i][j] = arrOfStr[j];
            }
        }
    } //07600431

    public void cal() {  //07600532
        Boolean check = false;
        for (int x = 0; x < word.length(); x++) {
            for (int i = 0; i < count_i; i++) {
                for (int j = 0; j < count_j + 2; j++) {
                    String WordChar = String.valueOf(word.charAt(x));
                    if (WordChar.equals(this.grammar[i][j])) {
                        check = true;
                        if (data[1][x + 1] == null) {
                            data[1][x + 1] = this.grammar[i][0];
                            data_Step[1][x + 1] = WordChar;
                            data_Step[1][x + 1] += " เลือกกฎ " + this.grammar[i][0];
                        } else {
                            data[1][x + 1] += "" + this.grammar[i][0];
                            data_Step[1][x + 1] += "," + this.grammar[i][0];
                        }
                    }
                }
            }
            if (check == false) {
                data[1][x + 1] = "Ø";
                data_Step[1][x + 1] = "ไม่เจอกฏ";
            }
            check = false;
        }
        for (int num = 1; num < word.length(); num++) { // word
            data_Step[2][num] += data[1][num] + " " + data[1][num + 1] + " ผลคูณคาร์ทีเซียนจะได้ "; /////-----------------
            for (int i = 0; i < data[1][num].length(); i++) { // สลับกัน
                for (int j = 0; j < data[1][num + 1].length(); j++) {  // สลับกัน
                    String step = data[1][num].charAt(i) + "" + data[1][num + 1].charAt(j); //// เก็บค่าแต่ละขั้นตอน                 
                    if (data[1][num].charAt(i) == 'Ø' || data[1][num + 1].charAt(j) == 'Ø') { ///////////////////////// แก้เพิ่ม
                        data[2][num] = "Ø";
                        step = "Ø";
                        data_Step[2][num] += step + ",";//////////---------------------                       
                        continue;
                    }
                    data_Step[2][num] += step + ",";  ///////////////-----------
                    data_position[2][num] += String.valueOf(1) + "," + String.valueOf(num) + "," + String.valueOf(1) + "," + String.valueOf(num + 1) + "#";////////*************************************
                    String convert = data[1][num].charAt(i) + "" + data[1][num + 1].charAt(j); //สลับสตริง
                    for (int x = 0; x < count_i; x++) { // เช็คกฎ
                        for (int y = 0; y < count_j + 2; y++) { //เช็คกฏ
                            if (convert.equals(this.grammar[x][y])) {
                                check = true;
                                if (data[2][num] == null) {
                                    data[2][num] = this.grammar[x][0];
                                } else {
                                    data[2][num] += "" + this.grammar[x][0];
                                }
                            }
                        }

                    }
                }
            }
            if (check == false) {
                data[2][num] = "Ø";
            }
            check = false;
        }
        for (int i = 3; i <= word.length(); i++) {
            for (int j = 1; j <= word.length(); j++) {
                for (int k = 1; k <= i - 1; k++) {   /////// ลูปที่ตำแหน่งที่ไม่เกิน  i 
                    String x = data[k][j];
                    String y = "";
                    if (j + k <= word.length()) {
                        y = data[i - k][j + k];
                        data_Step[i][j] += x + " " + y + " ผลคูณคาร์ทีเซียนจะได้ "; ///////////------------------
                    }
                    data_position[i][j] += String.valueOf(k) + "," + String.valueOf(j) + "," + String.valueOf(i - k) + "," + String.valueOf(j + k) + "#";////////*************************************
                    if (x != null && y != null) {
                        for (int l = 0; l < x.length(); l++) {
                            for (int m = 0; m < y.length(); m++) {
                                String step = x.charAt(l) + "" + y.charAt(m); /// เก็บค่าขั้นตอน                                                               
                                if (x.charAt(l) == 'Ø' || y.charAt(m) == 'Ø') {
                                    step = "Ø";
                                    data_Step[i][j] += step + ",";//////////---------------------
                                    continue;
                                }
                                data_Step[i][j] += step + ",";//////////---------------------
                                String convert = x.charAt(l) + "" + y.charAt(m); //สลับสตริง
                                for (int n = 0; n < count_i; n++) {
                                    for (int o = 0; o < count_j + 2; o++) {
                                        if (convert.equals(this.grammar[n][o])) {
                                            check = true;
                                            if (data[i][j] == null) {
                                                data[i][j] = this.grammar[n][0];
                                            } else {
                                                data[i][j] += "" + this.grammar[n][0];
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                    data_Step[i][j] += "-";
                }
                if (check == false) {
                    data[i][j] = "Ø";
                }
                check = false;
            }
        }
    } //07600532

    public void print_data() {
        for (int i = 1; i < word.length() + 1; i++) {
            for (int j = 1; j < word.length() + 2 - i; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println("");
        }

    }

    public void print_data_step() {
        System.out.println(data_position[2][2] + " ");
        String str[] = data_position[2][1].split("[#]+");

        for (int i = 0; i < str.length; i++) {
            System.out.println(str[i]);
        }
    }

    public String print_dataString() {
        String datatext = "";
        for (int i = 1; i < word.length() + 1; i++) {

            for (int j = 1; j < word.length() + 2 - i; j++) {
                datatext += data[i][j] + " ";
            }
            datatext += "\n";
        }
        return datatext;
    }

    public void print_grammar() { //ปรินไวยากร
        for (int i = 0; i < count_i; i++) {
            for (int j = 0; j < count_j + 2; j++) {
                System.out.print(grammar[i][j]);
            }
            System.out.println("");
        }
    }

    public void print_word() { //ปริน สายอักขระ
        System.out.println("word : " + word);
    }

    public String check_accept() { // รีเทินว่ายอมรับสายอักขระ 
        String check_accept = "";
        for (int i = 0; i < data[word.length()][1].length(); i++) { //วนลูป จาก 0 ถึง data[word.length()][1].length();
            if (data[word.length()][1].charAt(i) == 'S') { //ถ้าเจอ S
                check_accept = "สายอักขระ " + word + " ยอมรับ"; 
                System.out.println("สายอักขระ " + word + " ยอมรับ");
                checkpoint_accept = 1; //ให้เป็น 1 ถ้า ยอมรับ
                data_convert(); //เรียกใช้เมธอด data_convert();
                break; //หยุดลูปการทำงาน
            } else if (i == data[word.length()][1].length() - 1) { // i เท่ากับรอบสุดท้าย แล้วยังไม่เจอ S
                check_accept = "สายอักขระ " + word + " ไม่ยอมรับ";
                System.out.println("สายอักขระ " + word + " ไม่ยอมรับ");
            }

        }
        return check_accept; // รีเทินค่าcheck_accept
    } 

    public void data_convert() {  
        int count = 0;
        String store = "";
        String store2 = "";
        for (int i = 0; i < word.length(); i++) {

            String str1 = data[word.length() - i - 1][1]+","; // ตัวหน้า
            if (i == word.length()-2) {
                store += data[1][word.length() - i] + "," + data[1][word.length() - i - 1];
                store2 += data[1][word.length() - i] + "," + data[1][word.length() - i - 1];
                Store_convert[i] = store;
                StringBuffer sbr = new StringBuffer(store2);
                sbr.reverse();
                String x = sbr.toString();
                Store_convert[word.length()-1] = x;
                //System.out.println(sbr);
                break;
            }         
            store += data[1][word.length() - i] + ",";
            store2 += data[1][word.length() - i] +",";
            Store_convert[i] = str1 + "" + store;
            //System.out.println(str1 + " " + font);
        }
    } 
    public String print_data_convert(){
        String str = "S -> ";
        for (int i = 0; i < word.length()-1; i++) {
            str += Store_convert[i]+" -> ";
            //System.out.print(Store_convert[i]+" -> ");
        }
        str += Store_convert[word.length()-1]+" -> "+word;
        //System.out.println(str);
        return str;
        
    }
}

public class CYKsuper {

    /*public static void main(String[] args) {
        String word = "aaabbbcc";
        String test1 = "S -> AB\n"
                + "A -> CD | CF\n"
                + "B -> c | EB\n"
                + "C -> a\n"
                + "D -> b\n"
                + "E -> c\n"
                + "F -> AD";

        String word2 = "bbbabaaa";
        String test2 = "S -> SA\n"
                + "S -> AB\n"
                + "A -> BS\n"
                + "B -> SA\n"
                + "S -> a\n"
                + "A -> a\n"
                + "B -> b";
        CYK cyk = new CYK(word, test1);
        cyk.Put_grammar();
        cyk.cal();
        cyk.print_word();
        System.out.println("////////////////");
        cyk.print_data();
        System.out.println("////////////////");
        cyk.data_convert();
        cyk.print_data_convert();
    }*/

}
