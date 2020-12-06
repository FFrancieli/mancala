<template>
  <div class="container">
    <div v-if="gameStatus === 'ONGOING'" class="current-player">
      {{ currentPlayer }}'s turn
    </div>
    <div v-else class="current-player">
      {{ winner }} won
    </div>

    <Board :current-player="currentPlayer"
           :players="players"
           :pits="pits"
           @pit-clicked="notifyPitClicked($event)"/>

  </div>
</template>

<script>
import Board from "@/components/Board";
import gameService from '@/services/GameService'


export default {
  name: "GamePage",
  components: {Board},
  props: {
    gameId: {
      type: String,
      required: true
    },
    currentPlayer: {
      type: String,
      required: true
    },
    players: {
      type: Array,
      required: true
    },
    pits: {
      type: Array,
      required: true
    },
    gameStatus: {
      type: String,
      required: true
    },
    winner: {
      type: String,
      required: false
    }
  },
  methods: {
    notifyPitClicked(pitId) {
      this.$emit('pit-clicked', pitId);
    }
  },
}
</script>
