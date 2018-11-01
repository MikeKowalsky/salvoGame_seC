package edu.example.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {
//public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepo,
									  GameRepository gameRepo,
									  GamePlayerRepository gamePlayerRepo,
									  ShipRepository shipRepo,
									  SalvoRepository salvoRepo,
									  ScoreRepository scoreRepo) {
		return (args) -> {

			Player p1 = new Player("Jack Bauer", "j.bauer@ctu.gov", "24");
			Player p2 = new Player("Chloe O'Brian", "c.obrian@ctu.gov", "42");
			Player p3 = new Player("Kim Bauer", "kim_bauer@gmail.com", "kb");
			Player p4 = new Player("Tony Almeida", "t.almeida@ctu.gov", "mole");

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

			Ship s01 = new Ship("destroyer");
			s01.setLocations(new ArrayList<String>(Arrays.asList("H2", "H3", "H4")));
			gp1.addShip(s01);
			Ship s02 = new Ship("submarine");
			s02.setLocations(new ArrayList<String>(Arrays.asList("E2", "F2", "G2")));
			gp1.addShip(s02);
			Ship s03 = new Ship("patrolBoat");
			s03.setLocations(new ArrayList<String>(Arrays.asList("B4", "B5")));
			gp1.addShip(s03);
			Ship s04 = new Ship("aircraftCarrier");
			s04.setLocations(new ArrayList<String>(Arrays.asList("A1", "B1", "C1", "D1", "E1")));
			gp1.addShip(s04);
			Ship s05 = new Ship("battleship");
			s05.setLocations(new ArrayList<String>(Arrays.asList("D5", "E5", "F5", "G5")));
			gp1.addShip(s05);


			Ship s06 = new Ship("destroyer");
			s06.setLocations(new ArrayList<String>(Arrays.asList("B5", "C5", "D5")));
			gp2.addShip(s06);
			Ship s07 = new Ship("patrolBoat");
			s07.setLocations(new ArrayList<String>(Arrays.asList("C6", "C7")));
			gp2.addShip(s07);
			Ship s08 = new Ship("submarine");
			s08.setLocations(new ArrayList<String>(Arrays.asList("A3", "A4", "A5")));
			gp2.addShip(s08);
			Ship s09 = new Ship("battleship");
			s09.setLocations(new ArrayList<String>(Arrays.asList("E6", "F6" ,"G6", "H6")));
			gp2.addShip(s09);
			Ship s10 = new Ship("aircraftCarrier");
			s10.setLocations(new ArrayList<String>(Arrays.asList("A10", "B10", "C10", "D10", "E10")));
			gp2.addShip(s10);

			shipRepo.save(s01);
			shipRepo.save(s02);
			shipRepo.save(s03);
			shipRepo.save(s04);
			shipRepo.save(s05);
			shipRepo.save(s06);
			shipRepo.save(s07);
			shipRepo.save(s08);
			shipRepo.save(s09);
			shipRepo.save(s10);

			Salvo slv01 = new Salvo(1);
			slv01.setLocations(new ArrayList<String>(Arrays.asList("E6", "F6", "G6", "H6", "A2")));
			gp1.addSalvo(slv01);
			Salvo slv02 = new Salvo(2);
			slv02.setLocations(new ArrayList<String>(Arrays.asList("A3", "A4", "A5", "B7", "B8")));
			gp1.addSalvo(slv02);
			Salvo slv03 = new Salvo(3);
			slv03.setLocations(new ArrayList<String>(Arrays.asList("B5", "D5", "C5", "A9", "A8")));
			gp1.addSalvo(slv03);
			Salvo slv04 = new Salvo(4);
			slv04.setLocations(new ArrayList<String>(Arrays.asList("C6", "C7", "A1", "C1", "C2")));
			gp1.addSalvo(slv04);

			Salvo slv05 = new Salvo(1);
			slv05.setLocations(new ArrayList<String>(Arrays.asList("D5", "E5", "F5", "G5", "H5")));
			gp2.addSalvo(slv05);
			Salvo slv06 = new Salvo(2);
			slv06.setLocations(new ArrayList<String>(Arrays.asList("D2", "E2", "F2", "G2", "H8")));
			gp2.addSalvo(slv06);
			Salvo slv07 = new Salvo(3);
			slv07.setLocations(new ArrayList<String>(Arrays.asList("H2", "H3", "H4", "A2", "A3")));
			gp2.addSalvo(slv07);
			Salvo slv08 = new Salvo(4);
			slv08.setLocations(new ArrayList<String>(Arrays.asList("B4", "B5", "C10", "D10", "E10")));
			gp2.addSalvo(slv08);

			salvoRepo.save(slv01);
			salvoRepo.save(slv02);
			salvoRepo.save(slv03);
			salvoRepo.save(slv04);
			salvoRepo.save(slv05);
			salvoRepo.save(slv06);
			salvoRepo.save(slv07);
			salvoRepo.save(slv08);

			Score sc01 = new Score(p1, g1, 1.0);
			Score sc02 = new Score(p2, g1, 0.0);
			Score sc03 = new Score(p1, g2, 0.5);
			Score sc04 = new Score(p2, g2, 0.5);
			Score sc05 = new Score(p2, g3, 1.0);
			Score sc06 = new Score(p4, g3, 0.0);
			Score sc07 = new Score(p2, g4, 0.5);
			Score sc08 = new Score(p1, g4, 0.5);

			scoreRepo.save(sc01);
			scoreRepo.save(sc02);
			scoreRepo.save(sc03);
			scoreRepo.save(sc04);
			scoreRepo.save(sc05);
			scoreRepo.save(sc06);
			scoreRepo.save(sc07);
			scoreRepo.save(sc08);
		};
	}
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepo;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception{

//		-- > this one works < --
//		auth.inMemoryAuthentication()
//				.withUser("j.bauer@ctu.gov").password("{noop}24").roles("USER");

		auth.userDetailsService(inputName-> {
//			System.out.println("Passed userName: " + inputName);
			Player player = playerRepo.findByUserName(inputName);
//			System.out.println("Player: " + player);

			if (player != null) {
				return User.withDefaultPasswordEncoder()
					.username(player.getUserName())
					.password(player.getPassword())
					.roles("USER")
					.build();
			} else {
				System.out.println("Unknown user: " + inputName);
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
//			.antMatchers("/**").permitAll();
			.antMatchers("/web/games.html").permitAll()
			.antMatchers("/web/styles/games.css").permitAll()
			.antMatchers("/web/scripts/games.js").permitAll()
			.antMatchers("/web/scripts/handleTime.js").permitAll()
			.antMatchers("/web/scripts/loginFunctions.js").permitAll()
			.antMatchers("/api/games").permitAll()
			.antMatchers("/api/leaderboard").permitAll()
			.antMatchers("/api/players").permitAll()
			.antMatchers("/favicon.ico").permitAll()
			.antMatchers("/**").hasAuthority("USER");

		http.formLogin()
			.usernameParameter("userName")
			.passwordParameter("password")
			.loginPage("/api/login");

		http.logout()
			.logoutUrl("/api/logout");


		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}