const printGrids = data => {
  const columnArray = ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
  const rowArray = ["", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

  let vuePlayersGrid = new Vue({
    el: "#vueGrid",
    data: {
      columnArr: columnArray,
      rowArr: rowArray,
      dataIn: data,
      playersCellsArr: [],
      shipLocationsArrayToMark: [],
      shipType: null,
      arrToSend: [],
      showSaveShipsButton: false
    },
    computed: {
      opponentName() {
        return this.dataIn.opponent !== null ? this.dataIn.opponent.name : null;
      },
      shipAlreadyPlaced() {
        return this.dataIn.ships.length < 1;
      }
    },
    mounted() {
      this.addCellsIds("player");
      this.markShips();
      if (this.opponentName !== null) {
        this.addCellsIds("opponent");
        this.markSalvoes();
      }
    },
    methods: {
      addCellsIds(playerType) {
        const idsArray = [];
        let cellsArray;

        playerType == "player"
          ? (cellsArray = Array.from(
              document.querySelector("#playersGrid").querySelectorAll("td")
            ))
          : (cellsArray = Array.from(
              document.querySelector("#opponentsGrid").querySelectorAll("td")
            ));

        this.columnArr.forEach(letter => {
          playerType == "player"
            ? this.rowArr.forEach(number => idsArray.push(`${letter}${number}`))
            : this.rowArr.forEach(number =>
                idsArray.push(`s${letter}${number}`)
              );
        });

        idsArray.forEach((id, index) =>
          cellsArray[index].setAttribute("id", id)
        );
      },
      markShips() {
        const shipsLocations = [];
        this.dataIn.ships.forEach(ship =>
          ship.locations.forEach(loc => shipsLocations.push(loc))
        );
        shipsLocations.forEach(loc => {
          document.querySelector(`#${loc}`).classList.add("shipLoc");
        });
      },
      markSalvoes() {
        this.dataIn.salvoes.forEach(salvoSet => {
          if (salvoSet.player_id == this.dataIn.player.player_id) {
            salvoSet.locations.forEach(loc => {
              const td = document.querySelector(`#s${loc}`);
              td.classList.add("salvoLoc");
              td.innerHTML = salvoSet.turn;
            });
          } else {
            salvoSet.locations.forEach(loc => {
              const td = document.querySelector(`#${loc}`);
              td.classList.contains("shipLoc")
                ? td.classList.add("hitLoc")
                : td.classList.add("salvoLoc");
              td.innerHTML = salvoSet.turn;
            });
          }
        });
      },
      sendShips() {
        const url = `/api/games/players/${this.dataIn.player.gp_id}/ships`;

        fetch(url, {
          credentials: "include",
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json"
          },
          body: JSON.stringify(this.arrToSend)
        })
          .then(response => {
            console.log(response);
            if (!response.ok) {
              alert(`Error code: ${response.status}`);
              throw new Error(response.status);
            }
            location.reload();
          })
          .catch(err => console.log(err));
      },
      placeShipsButton() {
        this.playersCellsArr = Array.from(
          document.querySelector("#playersGrid").querySelectorAll("td")
        ).filter(td => td.id.length >= 2 && !td.id.charAt(0).match(/\d/));

        this.playersCellsArr.forEach(td =>
          td.addEventListener("mouseover", this.printMouseoverShipMark)
        );
      },
      printMouseoverShipMark(e) {
        // change name - this one is not proper
        const pointerLocation = e.target.id;
        // console.log(pointerLocation);

        //reset this.playersCellsArr - remove old ship position
        this.playersCellsArr.forEach(td =>
          td.classList.remove("shipOnMoveCorrect", "shipOnMoveError")
        );

        const shipTypeTemp = this.whichShipTypeToPlace();
        if (shipTypeTemp) {
          this.shipType = shipTypeTemp.slice(1);
          const shipSize = shipTypeTemp.slice(0, 1);
          const shipOrientation = this.whichShipOrientationToPlace(); // hor or ver
          this.shipLocationsArrayToMark = this.createShipLocationsArrayToMark(
            pointerLocation,
            shipSize,
            shipOrientation
          );
          if (
            this.verifyShipLocationsArrayToMark(this.shipLocationsArrayToMark)
          ) {
            this.showShipOnPlaceingCorrect(this.shipLocationsArrayToMark);
            // console.log(e.target);
            e.target.addEventListener("click", this.handleMouseClick);
          } else {
            this.showShipOnPlaceingError(this.shipLocationsArrayToMark);
          }
        }
      },
      whichShipTypeToPlace() {
        const shipTypeButton = document.querySelector(
          "input[name=shipType]:checked"
        );
        return shipTypeButton ? shipTypeButton.id : null;
      },
      whichShipOrientationToPlace() {
        return document.querySelector("input[name=shipOrientation]:checked").id;
      },
      createShipLocationsArrayToMark(pointer, size, orientation) {
        const arr = [];
        const pointerParts = [pointer.charAt(0), parseInt(pointer.slice(1))];
        for (let i = 0; i < size; i++) {
          if (orientation == "hor") {
            const colIndex = this.rowArr.indexOf(pointerParts[1]);
            arr.push(`${pointerParts[0]}${this.rowArr[colIndex + i]}`);
          } else {
            const rowIndex = this.columnArr.indexOf(pointerParts[0]);
            arr.push(`${this.columnArr[rowIndex + i]}${pointerParts[1]}`);
          }
        }
        return arr;
      },
      verifyShipLocationsArrayToMark(arr) {
        const isInGrid = arr[arr.length - 1].includes("undefined")
          ? false
          : true;

        if (isInGrid) {
          const onAnotherShip =
            arr.filter(id =>
              document.querySelector(`#${id}`).classList.contains("ship")
            ).length > 0
              ? false
              : true;
          return isInGrid && onAnotherShip;
        }
        return false;
      },
      showShipOnPlaceingCorrect(arr) {
        arr.forEach(td =>
          document.querySelector(`#${td}`).classList.add("shipOnMoveCorrect")
        );
      },
      showShipOnPlaceingError(arr) {
        arr
          .filter(td => !td.includes("undefined"))
          .forEach(td =>
            document.querySelector(`#${td}`).classList.add("shipOnMoveError")
          );
      },
      handleMouseClick(e) {
        e.preventDefault();
        console.log(`clicked ${e.target.id}`);
        const oneShipObject = {
          shipType: this.shipType,
          locations: this.shipLocationsArrayToMark
        };

        // adding to arr to send
        this.arrToSend.push(oneShipObject);
        console.log(this.arrToSend);

        // marking on a grid
        this.saveAndMarkShip();

        // disable this radio button
        const thisInput = Array.from(
          document.querySelectorAll("input[name=shipType]")
        ).filter(inp => inp.id.includes(this.shipType));
        thisInput[0].checked = false;
        thisInput[0].setAttribute("disabled", "disabled");
        document
          .querySelector(`.${this.shipType}`)
          .classList.add("strikethrough");

        // remove event listeners
        this.playersCellsArr.forEach(td =>
          td.removeEventListener("click", this.handleMouseClick)
        );

        //showSaveShipsButton
        if (this.arrToSend.length == 5) {
          this.showSaveShipsButton = true;
        }
      },
      saveAndMarkShip() {
        this.shipLocationsArrayToMark.forEach(td =>
          document.querySelector(`#${td}`).classList.add("ship")
        );
      }
    }
  });
};

const basicInfo = data => {
  let vueInfo = new Vue({
    el: "#vueInfo",
    data: {
      dataIn: data
    },
    computed: {
      creationDate() {
        const catchDate = this.dataIn.created.split("T");
        const catchTime = catchDate[1].split(".");
        const catchHour = catchTime[0].split(":");
        // console.log({catchDate,catchTime, catchHour})
        return `${catchDate[0]} at ${catchHour[0]}:${catchHour[1]}`;
      },
      playerName() {
        return this.dataIn.player.name;
      },
      opponentName() {
        return this.dataIn.opponent !== null ? this.dataIn.opponent.name : null;
      }
    },
    methods: {
      backToGameList() {
        window.location = "/web/games.html";
      }
    }
  });
};

const request = async url => {
  // const response = await fetch('/api/game_view/13')
  const response = await fetch(url);
  if (!response.ok) {
    const resposeJson = await response.json();
    document.querySelector("#allContent").innerHTML = resposeJson.error;
    // alert(`${ resposeJson.error } Error code: ${ response.status }`)
    throw new Error(resposeJson.error);
  }
  //   console.log(response);
  const data = await response.json();
  console.log({ data });
  basicInfo(data);
  printGrids(data);
};

const getApiURL = () => {
  const urlSearchPart = window.location.search;
  const tempArray = urlSearchPart.split("=");
  const myVar = tempArray[1];
  return `/api/game_view/${myVar}`;
};

const main = () => {
  const url = getApiURL();
  request(url);
};

onload = (() => main())();
