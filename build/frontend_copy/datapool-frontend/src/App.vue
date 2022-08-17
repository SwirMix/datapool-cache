<template>
  <v-app>
    <v-app-bar
      app
      dark
    >
      <h2>HttpDatapool</h2>
      <v-spacer></v-spacer>
      <DeleteProjectDialog class="mr-3 ma-3"/>
      <v-select
         class="mt-7 mr-4"
         :items="this.GET_PROJECTS()"
         label="Project"
         v-model="project"
      >
        <template v-slot:item="{item}">
          {{item.name}} [{{item.id}}]
        </template>
        <template v-slot:selection="{item}">
          {{item.name}} [{{item.id}}]
        </template>
      </v-select>
      <v-menu
        bottom
        min-width="200px"
        rounded
        offset-y
      >
        <template v-slot:activator="{ on }">
          <v-btn
            icon
            x-large
            v-on="on"
          >
            <v-avatar
              color="brown"
              size="48"
            >
              <span class="white--text text-h5">{{ user.initials }}</span>
            </v-avatar>
          </v-btn>
        </template>
        <v-card>
          <v-list-item-content class="justify-center">
            <div class="mx-auto text-center">
              <v-avatar
                color="brown"
              >
                <span class="white--text text-h5">{{ user.initials }}</span>
              </v-avatar>
              <h3>{{ user.fullName }}</h3>
              <p class="text-caption mt-1">
                {{ user.email }}
              </p>
              <v-divider class="my-3"></v-divider>
              <RemoteTokenDialog/>
              <v-divider class="my-3"></v-divider>
              <CreateProjectDialog/>
              <v-divider class="my-3"></v-divider>
              <v-btn
                depressed
                rounded
                text
                @click="logout()"
              >
                Logout
              </v-btn>
            </div>
          </v-list-item-content>
        </v-card>
      </v-menu>
    </v-app-bar>
    <v-main>
    <v-overlay
      :value="this.overlay"
    >
         <v-container fluid fill-height>
            <v-layout align-center justify-center>
                  <v-card style="width: 30rem;">
                     <v-toolbar dark color="primary">
                        <v-toolbar-title>Login form</v-toolbar-title>
                     </v-toolbar>
                     <v-card-text>
                        <v-form>
                           <v-text-field
                              prepend-icon="mdi-account"
                              name="login"
                              v-model="auth_data.login"
                              label="Login"
                              type="text"
                           ></v-text-field>
                           <v-text-field
                              id="password"
                              prepend-icon="mdi-lock"
                              name="password"
                              v-model="auth_data.pass"
                              label="Password"
                              type="password"
                           ></v-text-field>
                        </v-form>
                     </v-card-text>
                     <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" @click="auth()">Login</v-btn>
                     </v-card-actions>
                  </v-card>
            </v-layout>
         </v-container>
    </v-overlay>
    <router-view/>
    </v-main>
    <v-bottom-navigation v-model="value">
        <v-btn value="Http caches" text class="my-2" to="/">
          <span>Http caches</span>
          <v-icon>mdi-database</v-icon>
        </v-btn>
        <v-btn value="CSV storage" text class="my-2" to="/storage">
          <span>CSV storage</span>
          <v-icon>mdi-nas</v-icon>
        </v-btn>
        <v-btn value="Static parameters" text class="my-2" to="/params">
          <span>Static parameters</span>
          <v-icon>mdi-star-settings</v-icon>
        </v-btn>
        <v-btn value="Datapool cluster" text class="my-2" to="/cluster">
          <span>Datapool cluster</span>
          <v-icon>mdi-webpack</v-icon>
        </v-btn>
    </v-bottom-navigation>
  </v-app>
</template>

<script>
import apiClient from '@/plugins/backendClient.js'
import RemoteTokenDialog from '@/components/RemoteTokenDialog.vue'
import CreateProjectDialog from '@/components/CreateProjectDialog.vue'
import DeleteProjectDialog from '@/components/DeleteProjectDialog.vue'
import {mapGetters} from 'vuex'
import {mapActions} from 'vuex'
import {mapMutations} from 'vuex'

export default {
  name: 'App',

  components: {
    RemoteTokenDialog,
    CreateProjectDialog,
    DeleteProjectDialog
  },

  data: () => ({
    absolute: true,
    overlay: true,
    auth_data: {
       login: '',
       pass: ''
    },
    user: {
       initials: 'you',
       fullName: '',
       email: '',
    },
    project: ''
  }),
  watch: {
     project() {
        this.REFRESH_CURRENT_PROJECT(this.project)
     },
  },
  computed: {
  },
  methods: {
    ...mapGetters([
      'GET_OVERLAY',
      'GET_PROJECTS'
    ]),
    ...mapActions([
       'REFRESH_PROJECTS',
       'REFRESH_CURRENT_PROJECT'
    ]),
    ...mapMutations([
       'SET_CURRENT_PROJECT'
    ]),
    auth(){
        apiClient.auth(this.auth_data).then((response)=>{
           localStorage.setItem('http.datapool.token', response.data.token);
           localStorage.setItem('http.datapool.login', response.data.login);
           localStorage.setItem('http.datapool.email', response.data.email);
           console.log(response.data)
           this.overlay = false
           this.REFRESH_PROJECTS()
           this.$router.push({ path: '/datapools' })
        })
    },
    logout(){
       localStorage.removeItem('http.datapool.token');
       this.overlay=true
    },
    init(){
        if (localStorage.getItem('http.datapool.token')){
            this.overlay = false;
        } else {
            this.overlay = true;
        }
        this.REFRESH_PROJECTS()
        this.user.fullName = localStorage.getItem('http.datapool.login')
        this.user.email = localStorage.getItem('http.datapool.email')
    }
  },
  mounted(){
    this.init()
  },
  created(){
    this.init()
  }
};
</script>
