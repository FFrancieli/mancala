<template>
  <div class="board" id="kalah-board">
    <Kalah :amount-of-seeds="northOrientationKalah"></Kalah>
    <div class="section midsection">
      <div class="player-top player-name">
        {{ playerAssignedToNorthSideOfTheBoard }}
      </div>
      <PitRow :pits="northSideOrientationPits"
              :assigned-to-player="playerAssignedToNorthSideOfTheBoard"
              :current-player="currentPlayer"
              @pit-clicked="handlePitClickedEvent($event)"/>

      <PitRow :pits="southSideOrientationPits"
              :assigned-to-player="playerAssignedToSouthSideOfTheBoard"
              :current-player="currentPlayer"
              @pit-clicked="handlePitClickedEvent($event)"/>
    </div>
    <Kalah :amount-of-seeds="southOrientationKalah"></Kalah>
    <div class="player-bottom player-name">
      {{ playerAssignedToSouthSideOfTheBoard }}
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
  methods: {
    handlePitClickedEvent(eventData) {
      this.$emit('pit-clicked', eventData);
    },
  },
  computed: {
    northSideOrientationPits() {
      const pitsOnNorth = this.pits.filter(pit => pit.index > 6 && pit.index !== 13);
      return pitsOnNorth.sort(function (a, b) {
        return b.index - a.index;
      });
    },
    northOrientationKalah() {
      return this.pits.filter(pit => pit.index === 13)[0].totalSeeds;
    },
    southOrientationKalah() {
      return this.pits.filter(pit => pit.index === 6)[0].totalSeeds;
    },
    southSideOrientationPits() {
      return this.pits.filter(pit => pit.index <= 5);
    },
    playerAssignedToNorthSideOfTheBoard() {
      return this.players.filter(player => player.boardSide === "NORTH")[0].name
    },
    playerAssignedToSouthSideOfTheBoard() {
      return this.players.filter(player => player.boardSide === "SOUTH")[0].name;
    }
  }
}
</script>
