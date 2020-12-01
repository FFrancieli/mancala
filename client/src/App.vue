<template>
  <GamePage :current-player="currentPlayer"
            :players="players"
            :pits="pits"/>
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
      a: null,
      currentPlayer: '',
      players: [],
      pits: []
    }
  },
  beforeCreate() {
    gameService.startGame("John", "Jane")
        .then(response => {
          this.a = response
          this.currentPlayer = response.currentPlayer
          this.players = response.players
          this.pits = response.pits
        }).catch(err => {
      console.log(err);
    });
  }
}
</script>

<style>
@import './assets/style.css';
</style>
