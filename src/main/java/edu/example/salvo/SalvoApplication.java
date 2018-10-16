package edu.example.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepo,
									  GameRepository gameRepo,
									  GamePlayerRepository gamePlayerRepo) {
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

			gameRepo.save(g1);
			gameRepo.save(g2);
			gameRepo.save(g3);
			gameRepo.save(g4);

			GamePlayer gp1 = new GamePlayer(g1, p1);
			GamePlayer gp2 = new GamePlayer(g1, p2);
			GamePlayer gp3 = new GamePlayer(g2, p3);
			GamePlayer gp4 = new GamePlayer(g2, p4);
			GamePlayer gp5 = new GamePlayer(g3, p1);

			gamePlayerRepo.save(gp1);
			gamePlayerRepo.save(gp2);
			gamePlayerRepo.save(gp3);
			gamePlayerRepo.save(gp4);
			gamePlayerRepo.save(gp5);

		};
	}

}
