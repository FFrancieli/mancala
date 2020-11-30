<template>
  <div class="board" id="kalah-board">
    <Kalah :amount-of-seeds="northOrientationKalah.amountOfSeeds"></Kalah>
    <div class="section midsection">
      <div class="player-top player-name">
        {{ playerAssignedToNorthSideOfTheBoard.name }}
      </div>
      <PitRow :pits="northSideOrientationPits"
              :assigned-to-player="playerAssignedToNorthSideOfTheBoard"
              :current-player="currentPlayer"/>

      <PitRow :pits="southSideOrientationPits"
              :assigned-to-player="playerAssignedToSouthSideOfTheBoard"
              :current-player="currentPlayer"/>
    </div>
    <Kalah :amount-of-seeds="southOrientationKalah.amountOfSeeds"></Kalah>
    <div class="player-bottom player-name">
      {{ playerAssignedToSouthSideOfTheBoard.name }}
    </div>
  </div>
</template>

<script>
import PitRow from "@/components/PitRow";
import Kalah from "@/components/Kalah";

export default {
  name: 'Board',
  props: {
    currentPlayer: String,
    players: {
      type: Array,
      required: true
    },
    pits: {
      type: Array,
      required: true
    }
  },
  components: {Kalah, PitRow},
  computed: {
    northSideOrientationPits() {
      return this.pits.filter(pit => pit.index > 6 && pit.index !== 13);
    },

    northOrientationKalah() {
      return this.pits.filter(pit => pit.index === 13)[0];
    },

    southOrientationKalah() {
      return this.pits.filter(pit => pit.index === 6)[0];
    },

    southSideOrientationPits() {
      return this.pits.filter(pit => pit.index <= 5);
    },

    playerAssignedToNorthSideOfTheBoard() {
      return this.players.filter(player => player.orientation === "NORTH")[0];
    },
    playerAssignedToSouthSideOfTheBoard() {
      return this.players.filter(player => player.orientation === "SOUTH")[0];
    }
  }
}
</script>
