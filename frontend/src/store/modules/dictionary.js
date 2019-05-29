import VueCookies from 'vue-cookies';
import axios from 'axios';

const state = {
    specialities: [],
    genders: {},
    shiftOrders: {},
    auth: false,
    userId: "",
    patientId: "",
    doctorId: ""
};

const getters = {};

const mutations = {
    init(state, dictionaryData) {
        state.specialities = dictionaryData.specialities;
        state.genders = new Map(dictionaryData.genders.map(el => [el.value, el.label]));
        state.shiftOrders = new Map(dictionaryData.shiftOrders.map(el => [el.value, el.label]));
        state.auth = true;

        let authorizationToken = VueCookies.get('authorization');
        const arrayOfStrings = authorizationToken.split(".");
        const idBase64 = arrayOfStrings[1];

        var tokenData = JSON.parse(atob(idBase64));
        const tokenSub = tokenData.sub;
        let strings = tokenSub.split(":");
        state.userId = strings[1];
        state.userRole = strings[2];

        axios.get('/backend/user/' + state.userId, {
            headers: {
                Authorization: authorizationToken
            }
        }).then(response => {
            state.patientId = response.data.patientId;
            state.doctorId = response.data.doctorId;
        });
    }
};

export default {
    namespaced: true,
    state,
    getters,
    mutations
}
