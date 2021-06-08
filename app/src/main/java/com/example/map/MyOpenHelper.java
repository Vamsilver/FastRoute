package com.example.map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyOpenHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public MyOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String qUser = "CREATE TABLE user\n" +
                "\t(id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "      login TEXT NOT NULL UNIQUE,\n" +
                "      pass  TEXT NOT NULL,\n" +
                "      email TEXT NOT NULL UNIQUE)";
        db.execSQL(qUser);

        String qCategory = "CREATE TABLE category \n" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "    name TEXT  NOT NULL)";
        db.execSQL(qCategory);

        String qMarkers = "CREATE TABLE markers \n" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "    name        TEXT    NOT NULL,\n" +
                "    lat         FLOAT   NOT NULL,\n" +
                "    lot         FLOAT   NOT NULL,\n" +
                "    img         TEXT            ,\n" +
                "    description TEXT            ,\n" +
                "    idCategory  INTEGER NOT NULL,\n" +
                "    FOREIGN KEY(idCategory) REFERENCES category(id))";
        db.execSQL(qMarkers);



        db.execSQL("INSERT INTO user(login, pass, email) VALUES('test','test','test')");
        db.execSQL("INSERT INTO category(name) VALUES('The park')");
        db.execSQL("INSERT INTO category(name) VALUES('Plage')");
        db.execSQL("INSERT INTO category(name) VALUES('Hospitals')");
        db.execSQL("INSERT INTO category(name) VALUES('Dentistry')");
        db.execSQL("INSERT INTO category(name) VALUES('Pharmacy')");
        db.execSQL("INSERT INTO category(name) VALUES('Cafe')");
        db.execSQL("INSERT INTO category(name) VALUES('Bakery')");
        db.execSQL("INSERT INTO category(name) VALUES('Supermarket')");
        db.execSQL("INSERT INTO category(name) VALUES('Mall')");
        db.execSQL("INSERT INTO category(name) VALUES('Music store')");
        db.execSQL("INSERT INTO category(name) VALUES('Electronics store')");
        db.execSQL("INSERT INTO category(name) VALUES('Clothing store')");
        db.execSQL("INSERT INTO category(name) VALUES('Household store')");
        db.execSQL("INSERT INTO category(name) VALUES('Vape shop')");
        db.execSQL("INSERT INTO category(name) VALUES('ATM')");
        db.execSQL("INSERT INTO category(name) VALUES('Bank')");
        db.execSQL("INSERT INTO category(name) VALUES('WC')");
        db.execSQL("INSERT INTO category(name) VALUES('Railway station')");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Арыш мае',55.802527240180666, 49.17590849978377, 2)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Локомотив',55.78836715386363, 49.09519008910045, 2)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Молодежный центр',55.80833348841141, 49.09808578384585, 2)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Пляж',55.84246393457305, 48.974299559585695, 2)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Железнодорожная больница',55.804038289286154, 49.18177320158833, 3)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Гауз Кму',55.770932269540424, 49.109503764568885, 3)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Поликлиника №10',55.82120758549376, 49.1261549160774, 3)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Happy Clinic',55.80566843276913, 49.18537807785687, 4)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Семейная стоматология',55.83006869394564, 49.07869053248027, 4)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Центр стоматологии',55.78443837544963, 49.19516277726964, 4)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Советская аптека',55.78501504299431, 49.19550609670193, 5)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Будь Здоров!',55.78805546596522, 49.15885639457532, 5)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Сакура',55.800917558688404, 49.183174014395505, 5)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Макдоналдс',55.795762768759886, 49.175111294203575, 6)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Шаурма',55.80066126761534, 49.18254905974818, 6)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Татмак',55.808468079666596, 49.18165466358889, 6)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Добрая Столовая',55.807126561988596, 49.1828348356657, 6)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('ЖАР-СВЕЖАР',55.804865472595836, 49.18810269393934, 7)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Пекарня',55.80210375061171, 49.1867937760765, 7)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('МЕГА',55.782197440800815, 49.21350112143404, 9)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Парк Хаус',55.830121104305896, 49.11820098516698, 9)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Южный',55.7718885798153, 49.215849829225455, 9)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('МузТорг',55.79118537801284, 49.15594000069743, 10)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Sale Sound',55.7880004231716, 49.142035425065025, 10)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('ТопСаунд',55.79707203402574, 49.12744420910274, 10)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('DNS',55.78663919944394, 49.15079014413741, 11)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('М.Видео',55.82783191681039, 49.136885574152515, 11)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Эльдорадо',55.83139925712921, 49.06221287856641, 11)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('OSTIN',55.78189942408841, 49.2136181894525, 12)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Chak-Chak',55.79927146334322, 49.1892422804737, 12)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Zolla дисконт',55.76480936197527, 49.232844268938, 12)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Альпари',55.80031294592628, 49.19095886918253, 13)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Хозяюшка',55.78844320800038, 49.17259110320648, 13)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Аллергодом',55.78786410335179, 49.119032755488085, 13)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Smoke Bubbles',55.80161573717196, 49.18451555726289, 14)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Evilhaze',55.8102054083452, 49.18167777709889, 14)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Joesshop',55.788543503749544, 49.12073799173249, 14)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('ВТБ',55.80281693893632, 49.18718657960206, 15)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('СберБанк',55.800793799900994, 49.18270729069593, 15)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('СберБанк',55.76944068035125, 49.14622070730653, 15)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('СберБанк',55.800793799900994, 49.18270729069593, 16)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Тимер',55.78276105558268, 49.114729914693825, 16)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('ОТП',55.809515006866064, 49.1828794841902, 16)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Туалет',55.80410267649444, 49.18614103771598, 17)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Туалет',55.798217083544685, 49.154040362598955, 17)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Туалет',55.78470580097272, 49.127261188010046, 17)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Новаторов',55.8089149272858, 49.17626239714433, 18)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Ометьево',55.779578241621955, 49.168709297116806, 18)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Вахитово',55.76779875207324, 49.10759785143949, 18)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Арыш мае',55.802535596484084, 49.17590834524502, 8)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Парк Тысячелетия',55.78947225493246, 49.122736588284134, 1)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Парк им.Урицкого',55.84618121196272, 49.06162514869848, 1)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Центральный парк',555.80221003154512, 49.15500893085707, 1)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Парк Континент',55.83500027394979, 49.15775551268526, 1)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Магнит',55.8023523665701, 49.186808997271505, 8)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Йола',55.801278987571855, 49.18356888902106, 8)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Бехетле',55.79244962410873, 49.15440791259049, 8)");
        db.execSQL("INSERT INTO markers(name, lat, lot, idCategory) VALUES('Пятерочка',55.79013339411567, 49.166274004395106, 8)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Cursor c;
//        while (c.moveToNext()){
//            arr.add(new Category(c.getInt(0), c.getString(1))){
//        }
//        }
    }
//    ArrayList<Category> arr = new ArrayList<>();
//    class Category{
//        int id;
//        String name;
//        public Category(int id, String name){
//            this.id = id;
//            this.name = name;
//        }
//    }
}
