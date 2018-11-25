export const createGame = async () => {
  const response = await fetch("/api/games", {
    credentials: "include",
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/x-www-form-urlencoded"
    }
  });
  if (!response.ok) {
    const errorJSON = await response.json();
    alert(`${errorJSON.error} Error code: ${response.status}`);
    throw new Error(response.status);
  }
  const json = await response.json();
  console.log(json);
  // alert(createUrlNForNewGame(json))
  window.location = createUrlNForNewGameAndJoin(json);
};

const createUrlNForNewGameAndJoin = json =>
  `/web/game.html?gp=${json.new_GamePlayerID}`;

export const joinGame = async event => {
  const gameId = event.target.dataset.game_id;
  const response = await fetch(`/api/game/${gameId}/players`, {
    credentials: "include",
    method: "POST",
    headers: {
      Accept: "application/json",
      "Content-Type": "application/x-www-form-urlencoded"
    }
  });
  if (!response.ok) {
    const errorJSON = await response.json();
    alert(`${errorJSON.error} Error code: ${response.status}`);
    throw new Error(response.status);
  }
  const json = await response.json();
  // console.log(json)
  window.location = createUrlNForNewGameAndJoin(json);
};
