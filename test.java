import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class test {

    public static String weekdays[] = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    static class Time {
        int Hour;
        int Minute;
        int Second;

        public int getHour() {
            return this.Hour;
        }

        public int getMinute() {
            return this.Minute;
        }

        public int getSecond() {
            return this.Second;
        }

        public void setHour(int input) {
            this.Hour = input;
        }

        public void setMinute(int input) {
            this.Minute = input;
        }

        public void setSecond(int input) {
            this.Second = input;
        }

        public void increment (int input) {
            this.Minute += input;
            if (this.Minute >= 60) {
                this.Hour+=this.Minute / 60;
                this.Minute%=60;
            }
        }


    }

    static class Koma {
        int status;
        Time start;
        Time end;
        String name;

        public Koma() {
            start = new Time();
            end = new Time();
        }

        public int getStatus() {
            return this.status;
        }

        public String getName() {
            return this.name;
        }

        public void setStatus(int input) {
            this.status = input;
        }

        public void setName(String input) {
            this.name = input;
        }
    }

    public static boolean compareTime(Time a, Time b) {

        if (a.getHour() < b.getHour()) return false;

        else if (a.getMinute() < b.getMinute()) return false;

        else if (a.getMinute() < b.getMinute()) return false;

        else return true;

    }

    public static void registerSchedule(Koma[][] input) {
        for ( int i = 0; i < 5 ; i++ ) {
            System.out.println("\nRegistering " + weekdays[i] + "...");
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            for (int j = 0; j < 6 ; j++) {
                System.out.println("Is koma " + (j + 1) + " registered?");
                int temp = 0;

                try {
                    temp = Integer.parseInt(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (temp > 0) {
                    try {
                        input[i][j].setStatus(temp);
                        System.out.println("name :");
                        input[i][j].setName(br.readLine());
                        System.out.println();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        }
    }

    public static void calendarToTime(Calendar calendar, Time time) {
        time.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        time.setMinute(calendar.get(Calendar.MINUTE));
        time.setSecond(calendar.get(Calendar.SECOND));
    }

    public static Koma processKoma(Koma[][] input, int flag) {

        Calendar currentCalendar = new GregorianCalendar();

        Time currTime = new Time();

        int weekday = 0;

        if (flag == 0) {
            weekday = currentCalendar.get(Calendar.DAY_OF_WEEK);
            weekday += 5;
        } else {
            weekday = currentCalendar.get(Calendar.DAY_OF_WEEK);
            weekday += 6;
        }

        weekday %= 7;

        if (weekday > 5) return null;

        calendarToTime(currentCalendar, currTime);

        for (int i = 0; i < 6; i++) {

            if (compareTime(currTime, input[weekday][i].start)) {

                if (compareTime(currTime, input[weekday][i].end)) {
                    if (i == 5) return null;
                    else return input[weekday][i + 1];
                }

                else return input[weekday][i];
            }

        }

        return null;

    }

    public static void main(String[] args) {

        // make new koma matrix

        Koma[][] schedule = new Koma[5][6];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                schedule[i][j] = new Koma();
            }
        }

        // set times

        int breakA = 10;
        int breakB = 55;
        int classDur = 90;
        int breakDur;

        for (int i = 0; i < 5; i++) {
            schedule[i][0].start.setHour(8);
            schedule[i][0].start.setMinute(40);
            schedule[i][0].end.setHour(10);
            schedule[i][0].end.setMinute(10);
                for (int j = 1; j < 6; j++) {
                    if ( j==2 ) breakDur = breakB;
                    else breakDur = breakA;
                    schedule[i][j].start.setHour(schedule[i][j - 1].end.getHour());
                    schedule[i][j].start.setMinute(schedule[i][j - 1].end.getMinute());
                    schedule[i][j].start.increment(breakDur);
                    schedule[i][j].end.setHour(schedule[i][j].start.getHour());
                    schedule[i][j].end.setMinute(schedule[i][j].start.getMinute());
                    schedule[i][j].end.increment(classDur);
                }

        }

        // register data

        registerSchedule(schedule);

        // search

        Koma output = processKoma(schedule, 0);

        // output

        Calendar currentTime = new GregorianCalendar();

        if (output==null) {
            System.out.println("Go home");
            output = processKoma(schedule, 1);
            System.out.println(output.start.getHour() + ":" + output.start.getMinute());
        } else {

            Time temp = new Time();
            calendarToTime(currentTime,temp);
            if (compareTime(temp, output.end)) {
                System.out.println("This class ends: ");
                System.out.println(output.end.getHour() + ":" + output.end.getMinute());
            } else {
                System.out.println("Next class starts: ");
                System.out.println(output.start.getHour() + ":" + output.start.getMinute());
            }
        }

    }
}
