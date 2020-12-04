<template>
  <GamePage :current-player="currentPlayer"
            :players="players"
            :pits="pits"
            :winner="winner"
            :game-status="gameStatus"
            @pit-clicked="test($event)"
            :key="componentKey"/>
</template>

<script>
import GamePage from "@/components/GamePage";
import gameService from '@/services/GameService'

export default {
  name: 'App',
  components: {
    GamePage
  },
  data() {
    return {
      componentKey: 1,
      gameId: '',
      currentPlayer: '',
      players: [],
      pits: [],
      gameStatus: '',
      winner: ''
    }
  },
  beforeCreate() {
    gameService.startGame("Player 1", "Player 2")
        .then(response => {
          this.gameId = response.id
          this.currentPlayer = response.currentPlayer
          this.players = response.players
          this.pits = response.pits
          this.gameStatus = response.gameStatus
          this.winner = response.winner
          this.componentKey += 1;
        }).catch(err => {
      console.log(err);
    });
  },
  methods: {
    test(pitId) {
      gameService.sowSeeds(this.gameId, pitId)
          .then(response => {
            this.gameId = response.id
            this.currentPlayer = response.currentPlayer
            this.players = response.players
            this.pits = response.pits
            this.gameStatus = response.gameStatus
            this.winner = response.winner
          });
    }
  }
}
</script>

<style>
@import './assets/style.css';
</style>
