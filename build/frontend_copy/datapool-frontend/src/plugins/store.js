import Vue from 'vue'
import Vuex from 'vuex'
import apiClient from "./backendClient.js"

Vue.use(Vuex);
export default new Vuex.Store({
    state: {
      current_project: '',
      projects: [],
      caches: [],
      storage: [],
      static_caches: []
    },
    mutations: {
      SET_STATIC_CACHE(state, caches){
        state.static_caches = caches
      },
      SET_CACHES(state, caches){
        state.caches = caches
      },
      SET_CURRENT_PROJECT(state, project){
        state.current_project = project;
      },
      SET_PROJECTS(state, projects){
        state.projects = projects
      },
      SET_STORAGE(state, storage){
        state.storage = storage
      }
    },
    actions: {
       REFRESH_PROJECTS({commit}){
          apiClient.get_projects().then((response)=>{
             console.log(response.data)
             commit('SET_PROJECTS', response.data);
             commit('SET_CURRENT_PROJECT', response.data[0])
          });
       },
       REFRESH_CACHES({commit}, project_id){
          apiClient.get_caches(project_id).then((response)=>{
             console.log(response.data)
             commit('SET_CACHES', response.data);
          })
       },
       REFRESH_CURRENT_PROJECT({commit}, project){
          apiClient.get_caches(project.id).then((response)=>{
             commit('SET_CURRENT_PROJECT', project)
             commit('SET_CACHES', response.data);
          }),
          apiClient.get_storage(project.id).then((response)=>{
            commit('SET_STORAGE', response.data)
          })
          apiClient.get_static_data_caches(project.id).then((response)=>{
            commit('SET_STATIC_CACHE', response.data)
          })
       }
    },
    getters: {
      GET_CURRENT_PROJECT(state){
        return state.current_project
      },
      GET_PROJECTS(state){
        return state.projects
      },
      GET_CACHES(state){
        return state.caches
      },
      GET_STORAGE(state){
        return state.storage
      },
      GET_STATIC_CACHES(state){
        return state.static_caches
      }
    }
})