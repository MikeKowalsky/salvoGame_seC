package edu.example.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepo,
									  GameRepository gameRepo,
									  GamePlayerRepository gamePlayerRepo,
									  ShipRepository shipRepo) {
		return (args) -> {

			Player p1 = new Player("Jack Bauer", "j.bauer@ctu.gov");
			Player p2 = new Player("Chloe O'Brian", "c.obrian@ctu.gov");
			Player p3 = new Player("Kim Bauer", "kim_bauer@gmail.com");
			Player p4 = new Player("Tony Almeida", "t.almeida@ctu.gov");

			playerRepo.save(p1);
			playerRepo.save(p2);
			playerRepo.save(p3);
			playerRepo.save(p4);

			Game g1 = new Game();
			Game g2 = new Game();
			Game g3 = new Game();
			Game g4 = new Game();
			Game g5 = new Game();
			Game g6 = new Game();
			Game g7 = new Game();
			Game g8 = new Game();

			gameRepo.save(g1);
			gameRepo.save(g2);
			gameRepo.save(g3);
			gameRepo.save(g4);
			gameRepo.save(g5);
			gameRepo.save(g6);
			gameRepo.save(g7);
			gameRepo.save(g8);

			GamePlayer gp1 = new GamePlayer(g1, p1);
			GamePlayer gp2 = new GamePlayer(g1, p2);
			GamePlayer gp3 = new GamePlayer(g2, p1);
			GamePlayer gp4 = new GamePlayer(g2, p2);
			GamePlayer gp5 = new GamePlayer(g3, p2);
			GamePlayer gp6 = new GamePlayer(g3, p4);
			GamePlayer gp7 = new GamePlayer(g4, p2);
			GamePlayer gp8 = new GamePlayer(g4, p1);
			GamePlayer gp9 = new GamePlayer(g5, p4);
			GamePlayer gp10 = new GamePlayer(g5, p1);
			GamePlayer gp11 = new GamePlayer(g6, p3);
			GamePlayer gp12 = new GamePlayer(g7, p4);
			GamePlayer gp13 = new GamePlayer(g8, p3);
			GamePlayer gp14 = new GamePlayer(g8, p4);

			gamePlayerRepo.save(gp1);
			gamePlayerRepo.save(gp2);
			gamePlayerRepo.save(gp3);
			gamePlayerRepo.save(gp4);
			gamePlayerRepo.save(gp5);
			gamePlayerRepo.save(gp6);
			gamePlayerRepo.save(gp7);
			gamePlayerRepo.save(gp8);
			gamePlayerRepo.save(gp9);
			gamePlayerRepo.save(gp10);
			gamePlayerRepo.save(gp11);
			gamePlayerRepo.save(gp12);
			gamePlayerRepo.save(gp13);
			gamePlayerRepo.save(gp14);

//			Ship s01 = new Ship("destroyer");
//			s01.setLocations(new ArrayList<String>(Arrays.asList("H2", "H3", "H4")));
//			gp1.addShip(s01);
//			Ship s02 = new Ship("submarine");
//			s02.setLocations(new ArrayList<String>(Arrays.asList("E2", "F2", "G2")));
//			gp1.addShip(s02);
//			Ship s03 = new Ship("patrolBoat");
//			s03.setLocations(new ArrayList<String>(Arrays.asList("B4", "B5")));
//			gp1.addShip(s03);
//			Ship s04 = new Ship("aircraftCarrier");
//			s04.setLocations(new ArrayList<String>(Arrays.asList("A1", "B1", "C1", "D1", "E1")));
//			gp1.addShip(s04);
//			Ship s05 = new Ship("battleship");
//			s05.setLocations(new ArrayList<String>(Arrays.asList("D5", "E5", "F5", "G5")));
//			gp1.addShip(s05);
//
//
//			Ship s06 = new Ship("destroyer");
//			s06.setLocations(new ArrayList<String>(Arrays.asList("B5", "C5", "D5")));
//			gp2.addShip(s06);
//			Ship s07 = new Ship("patrolBoat");
//			s07.setLocations(new ArrayList<String>(Arrays.asList("C6", "C7")));
//			gp2.addShip(s07);
//			Ship s08 = new Ship("submarine");
//			s08.setLocations(new ArrayList<String>(Arrays.asList("A3", "A4", "A5")));
//			gp2.addShip(s08);
//			Ship s09 = new Ship("battleship");
//			s09.setLocations(new ArrayList<String>(Arrays.asList("E6", "F6" ,"G6", "H6")));
//			gp2.addShip(s09);
//			Ship s10 = new Ship("aircraftCarrier");
//			s10.setLocations(new ArrayList<String>(Arrays.asList("A10", "B10", "C10", "D10", "E10")));
//			gp2.addShip(s10);
//
//			shipRepo.save(s01);
//			shipRepo.save(s02);
//			shipRepo.save(s03);
//			shipRepo.save(s04);
//			shipRepo.save(s05);
//			shipRepo.save(s06);
//			shipRepo.save(s07);
//			shipRepo.save(s08);
//			shipRepo.save(s09);
//			shipRepo.save(s10);

		};
	}

}
