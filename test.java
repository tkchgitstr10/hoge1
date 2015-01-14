import java.io.*;
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
            // return this.Second;
            return 0;
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

    public static void prepareRead(File file, Koma[][] input) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
        } catch(Exception e) {

        } finally {
            try {
                registerFromFile(in, input);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void prepareWrite(File file, Koma[][] input, int activeDays, int komaCount) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        } catch(Exception e) {

        } finally {
            saveToFile(pw, input, activeDays, komaCount);
            pw.close();
        }

    }

    public static void registerFromFile(BufferedReader in, Koma[][] input) throws IOException {
        String buffer = new String();
        int weekday = 0;
        int koma = 0;

        do {
            buffer = in.readLine();
            try {
                if (Integer.parseInt(buffer) >= 10000) {
                    weekday = (Integer.parseInt(buffer) / 10000) - 1;
                } else if (Integer.parseInt(buffer) >= 100) {
                    koma = (Integer.parseInt(buffer) / 100) - 1;
                }

            } catch (NumberFormatException e) {
                if (!(buffer == null)) {
                    input[weekday][koma].setStatus(1);
                    input[weekday][koma].setName(buffer);
                }
            }

        } while (!(buffer == null));
    }

    public static void saveToFile(PrintWriter pw, Koma[][] input, int activeDays, int komaCount) {
        for (int i = 0; i < activeDays; i++) {
            pw.println((i+1)*10000);
            for (int j = 0; j < komaCount; j++) {
                if (input[i][j].getStatus() == 1) {
                    pw.println((j+1)*100);
                    pw.println(input[i][j].getName());
                }
            }
        }
    }

    public static void calendarToTime(Calendar calendar, Time time) {
        time.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        time.setMinute(calendar.get(Calendar.MINUTE));
        time.setSecond(calendar.get(Calendar.SECOND));
    }

    public static Koma processHelper(Koma[][] input, Time currTime, int weekday, int komaCount) {
        Koma temp;
        for (int i = 0; i < komaCount; i++) {

            temp = input[weekday][i];

            if (compareTime(currTime, temp.start)) {

                if (compareTime(currTime, temp.end)) {
                    // currTime is more than both start and end, go to next i
                    // or, if end of day is reached, return null
                    if ( i == komaCount - 1 ) return null;
                }

                else if (temp.getStatus() == 1) return temp;
                else {
                    currTime.increment(countIncrement(currTime, temp.end));
                    processHelper(input, currTime, weekday, komaCount);
                }
            }

        }
        // end of day, no komas found
        return null;
    }

    public static int countIncrement(Time a, Time b) {
        return (b.getHour() - a.getHour()) * 60 + (b.getMinute() - a.getMinute()) + 11;
    }

    public static Koma processKoma(Koma[][] input, int flag, int komaCount) {

        Calendar currentCalendar = new GregorianCalendar();

        Time currTime = new Time();

        int weekday;

        // shift GregorianCalendar DAY_OF_WEEK so that Monday is 0

        if (flag == 0) {
            weekday = currentCalendar.get(Calendar.DAY_OF_WEEK);
            weekday += 5;
        } else {
            weekday = currentCalendar.get(Calendar.DAY_OF_WEEK);
            weekday += 6;
        }

        weekday %= 7;

        if (weekday > komaCount && komaCount < 7) return null;

        calendarToTime(currentCalendar, currTime);

        Koma output = processHelper(input, currTime, weekday, komaCount);

        return output;

    }

    public static void main(String[] args) {

        // settings

        int breakA = 10;
        int breakB = 55;
        int classDur = 90;
        int breakDur;

        int KOMACOUNT = 6;
        int ACTIVEDAYS = 5;


        // make new koma matrix

        Koma[][] schedule = new Koma[ACTIVEDAYS][KOMACOUNT];

        for (int i = 0; i < ACTIVEDAYS; i++) {
            for (int j = 0; j < KOMACOUNT; j++) {
                schedule[i][j] = new Koma();
            }
        }

        // set times

        for (int i = 0; i < ACTIVEDAYS; i++) {
            schedule[i][0].start.setHour(8);
            schedule[i][0].start.setMinute(40);
            schedule[i][0].end.setHour(10);
            schedule[i][0].end.setMinute(10);
                for (int j = 1; j < KOMACOUNT; j++) {
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

        File out = new File("tempfile");

        if (out.exists()) {
            prepareRead(out, schedule);
        } else {
            registerSchedule(schedule);
            prepareWrite(out, schedule, ACTIVEDAYS, KOMACOUNT);
        }

        // search

        Koma output = processKoma(schedule, 0, KOMACOUNT);

        // output

        Calendar currentTime = new GregorianCalendar();

        if (output==null) {
            System.out.println("Go home");
            output = processKoma(schedule, 1, KOMACOUNT);
            System.out.println(output.start.getHour() + ":" + output.start.getMinute());
        } else {

            Time temp = new Time();
            calendarToTime(currentTime,temp);
            if (compareTime(temp, output.end)) {
                System.out.println("This class ends: ");
                System.out.println(output.end.getHour() + ":" + output.end.getMinute() + ":" + output.end.getSecond());
                System.out.print("Class name: ");
                System.out.println(output.getName());
            } else {
                System.out.println("Next class starts: ");
                System.out.println(output.start.getHour() + ":" + output.start.getMinute() + ":" + output.end.getSecond());
            }
        }

    }
}
