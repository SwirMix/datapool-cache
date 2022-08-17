import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from './plugins/router'
import store from "./plugins/store.js"

Vue.config.productionTip = false
Vue.use(router)

new Vue({
  vuetify,
  store,
  router,
  render: h => h(App)
}).$mount('#app')
