import java.util.*;
	/*
	* Сделал все в одном файле.
	* С русскими коментами не компилится.
	* Если хочешь запускать, удаляй коменты.
	*/
public class Main{
	
	// Глобальной переменной объявлено дано
	// Сделано это с целью более красивого вывода в консоль
	final static int [][] data = {
		{10, 20, 30, 40, 50, 60, 70, 80, 90}, // x
		{40, 30, 20, 10, 70, 80, 90, 60, 50}  // y
	};

	// Обычный рандомизатор
	// Получает два числа и возвращает случайное целое число в их диапазоне
	static int random(int min, int max){
		max -= min;
		return (int) (Math.random() * ++max) + min;
	}

	// Возвращает два неодинаковых случайных числа в диапазоне [0 : `длина поданного вектора` - 1]
	static int [] getTwoRandomNumbers(int [] vector){
		int number1 = random(0, vector.length - 1);
		int number2;
		do{
			number2 = random(0, vector.length - 1);
		} while (number2 == number1);
		return new int [] {number1, number2};
	}

	// Функция для вывода вектора в консоль
	static void showVector(int [] vector){
		for (int i = 0; i < vector.length; i ++){
			System.out.print(vector[i] + " ");
		}
		if (vector.length != 2)
			System.out.print(vector[0] + " " + getLengthTrack(vector, data));
		System.out.println();
	}

	// Меняет местами два элемента вектора с указанными индексами
	static int [] replaceTwoElements(int [] vector, int [] numbers){
		vector[ numbers[0] ] = vector[ numbers[0] ] + vector[ numbers[1] ] - ( vector[ numbers[1] ] = vector[ numbers[0] ] );
		return vector;
	}

	// Функция "мутация". Одна из самых основных в программе
	// Благодаря ей и происходит оптимизация
	static int [] mutation(int [] vector){
		int [] numbers = getTwoRandomNumbers(vector);
		vector = replaceTwoElements(vector, numbers);
		return vector;
	}

	// Возвращает true или false в зависимости от того,
	// присутствует ли указанный элемент в указанном массиве
	static boolean in(int number, int [] array){
		for (int i = 0; i < array.length; i ++){
			if (array[i] == number){
				return true;
			}
		}
		return false;
	}

	// Генерирует рандомный маршрут
	static int [] getRandomTrack(int [][] data){
		int [] track = new int [data[0].length];
		int number = -1;
		for (int i = 0; i < track.length; i ++){
			track[i] = number;
			number --;
		}
		for (int i = 0; i < track.length; i ++){
			do{
				number = random(0, track.length - 1);
			} while ( in(number, track) );
			track[i] = number;
		}
		return track;
	}

	// Возвращает длину указанного маршрута
	static double getLengthTrack(int [] track, int [][] data){
		double length = 0;
		for (int i = 0; i < track.length - 1; i++){
			length += Math.sqrt(Math.pow((data[0][ track[i] ] - data[0][ track[i + 1] ]), 2) + Math.pow(data[1][ track[i] ] - data[1][ track[i + 1] ], 2));
		}
		return length;
	}



	public static void main(String[] args){
		
		// Объявляется двумерный массив маршрутов
		// Я выбрал, что всего будет 4 генома
		// В каждом геноме соответственно такое количество генов,
		// какое число городов рассматривается в задаче
		int [][] p = new int [4][data[0].length];
		for (int i = 0; i < p.length; i ++){
			p[i] = getRandomTrack(data);
		}

		// Вывод в консоль первоначально сгенерированных геномов
		for (int i = 0; i < p.length; i ++){
			showVector(p[i]);
		}
		System.out.println();

		// Инициализация переменных для оптимального маршрута и маршрута после применения мутаций
		int [] etalonTrack  = new int [data[0].length];
		int [] newTrack     = new int [data[0].length];
		do{
			// Определяем массив длин
			double [] lengths = new double [p.length];
			for (int i = 0; i < p.length; i ++){
				lengths[i] = getLengthTrack(p[i], data);
			}

			// Ищем маршрут с наименьшей длиной, запоминаем его номер
			int index = 0;
			for (int i = 0; i < p.length; i ++){
				if (lengths[i] < lengths[index]){
					index = i;
				}
			}
			double etalonLength = lengths[index];

			// Записываем в оптимальный маршрут значения
			etalonTrack  = new int [data[0].length];
			for (int i = 0; i < etalonTrack.length; i ++){
				etalonTrack[i] = p[index][i];
			}

			// Выводим его в консоль
			showVector(etalonTrack);
			System.out.println();

			// Применяем к лучшему маршруту мутации
			for (int i = 0; i < p.length; i ++){
				p[i] = Arrays.copyOf(etalonTrack, etalonTrack.length);
				p[i] = mutation(p[i]);
				showVector(p[i]);
			}
			
			// Вычисляем длины мутровавших геномов
			for (int i = 0; i < p.length; i ++){
				lengths[i] = getLengthTrack(p[i], data);
			}

			// Ищем номер минимальной длины
			index = 0;
			for (int i = 0; i < p.length; i ++){
				if (lengths[i] < lengths[index]){
					index = i;
				}
			}
			double newLength = lengths[index];

			// Записываем лучший мутировавший геном
			newTrack  = new int [data[0].length];
			for (int i = 0; i < newTrack.length; i ++){
				newTrack[i] = p[index][i];
			}

			// Вывод его в консоль
			showVector(newTrack);
			System.out.println();

			// Проверка условия на окончание проведения расчетов
		} while(getLengthTrack(newTrack, data) < getLengthTrack(etalonTrack, data));

		// Вывод в консоль лучшего расчитанного варианта
		System.out.println("------------------------------------");
		showVector(etalonTrack);
	}
}