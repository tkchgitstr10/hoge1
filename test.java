import java.util.*;

class test {

	public static String weekdays[] = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

	class Koma {
		int status;
		int startHour;
		int startMinute;
		String name;

		public int getStatus() {
			return status;
		}

		public int getHour() {
			return startHour;
		}

		public int getMinute() {
			return startMinute;
		}

		public String getName() {
			return name;
		}

		public void setHour(int input) {
			startHour = input;
		}

		public void setMinute(int input) {
			startMinute = input;
		}

		public void setStatus(int input) {
			status = input;
		}

		public void setName(String input) {
			name = input;
		}
	}

	public static int findWeekday(int y, int m, int d) {

		if (m == 1) {
            m = 13;
            y--;
        } else if (m == 2) {
            m = 14;
            y--;
        }
        
        int c = y / 100;

        int cy = y % 100;

        double h = (d + ((26*(m + 1)) / 10) + cy + (cy / 4) + (c / 4) + (5 * c)) % 7; // magic
        return (int)((h+3)%7);
	}

	public static void registerSchedule(Koma[][] input) {
		for ( int i = 0; i < 5 ; i++ ) {
			System.out.println("\nRegistering " + weekdays[i] + "...");
			for (int j = 0; j < 13 ; j++) {
				System.out.println("Is koma " + j + " registered?");
				Scanner in = new Scanner(System.in);
				int temp = in.nextInt();
				if (temp > 0) {
					input[i][j].setStatus(in.nextInt());
					System.out.println("startHour :");
					input[i][j].setHour(in.nextInt());
					System.out.println("startMinute :");
					input[i][j].setMinute(in.nextInt());
					System.out.println("name :");
					input[i][j].setName(System.console().readLine());
					System.out.println();
				}
			}
		}
	}

	public static void buildKomaTime(Calendar target, Koma koma) {

		Calendar currentTime = new GregorianCalendar();

		target.set(currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH) + 1, currentTime.get(Calendar.DAY_OF_MONTH), koma.getHour(), koma.getMinute(), 0);
	}

	public static Koma getNextKoma(Koma[][] input, int flag) {

		Koma temp = null;

		Calendar komaTime = new GregorianCalendar();

		for ( int i = 0; i < 13 ; i++ ) {

			int akiflag = 1;

			temp = input[findWeekday(komaTime.get(Calendar.YEAR), komaTime.get(Calendar.MONTH), komaTime.get(Calendar.DAY_OF_MONTH))][i];

			buildKomaTime(komaTime, temp);

			if (flag == 1) {

				Calendar curr = new GregorianCalendar();

				curr.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH) + 1, curr.get(Calendar.DAY_OF_MONTH) + 1, curr.get(Calendar.HOUR_OF_DAY), curr.get(Calendar.MINUTE), 0);

				if (curr.getTimeInMillis() < komaTime.getTimeInMillis()) {
					if (temp.getStatus() == 1) { 
						return temp;
					} else akiflag = 0;
				}

			} else if (((new GregorianCalendar()).getTimeInMillis() < komaTime.getTimeInMillis()) && (temp.getStatus() == 1)) return temp;

		}

		temp = null;

		komaTime = null;

		return null;

	}

	public static void main(String[] args) {
		
		// make new koma matrix

		Koma[][] schedule = new Koma[5][13];

		// register data

		registerSchedule(schedule);

		// search

		Koma output = getNextKoma(schedule, 0);

		// output

		Calendar currentTime = new GregorianCalendar();

		if (output==null) {
			System.out.print("Go home");
			output = getNextKoma(schedule, 1);
		} else {
			GregorianCalendar nextKomaTime = new GregorianCalendar();
			buildKomaTime(nextKomaTime, output);
			System.out.print("Next class starts in: ");
			System.out.print(nextKomaTime.getTimeInMillis() - System.currentTimeMillis());
			System.out.print("ms\n");
		}

	}
	
}