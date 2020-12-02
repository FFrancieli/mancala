import axios from 'axios'

const GAME_URL = 'http://localhost:8080/api/game/';

export default {
    startGame(firstPlayer, secondPlayer) {
        const requestBody = {
            "firstPlayerName": firstPlayer,
            "secondPlayerName": secondPlayer
        }

        return axios.post(GAME_URL, requestBody)
            .then(response => {
                return response.data;
            });
    },
    sowSeeds(gameId, pitIndex) {
        const requestParameters = {params: {pitIndex: pitIndex}};
        return axios.put(GAME_URL + gameId, null, requestParameters)
            .then(response => {
                return response.data;
            });
    }
}
