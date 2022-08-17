<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="80rem"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          depressed
          rounded
          text
          v-bind="attrs"
          v-on="on"
        >
          Create new project space
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Create Project
        </v-card-title>
        <v-card-text>
          <v-text-field
            label="Project name"
            v-model="name"
          ></v-text-field>
          <v-textarea
            solo
            name="input-7-4"
            label="Project description"
            v-model="description"
          ></v-textarea>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="success"
            text
            @click="create_project()"
          >
            Create
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
  import apiClient from '@/plugins/backendClient.js'
  import {mapActions} from 'vuex'

  export default {
    data () {
      return {
        dialog: false,
        name: '',
        description: ''
      }
    },
    methods: {
      ...mapActions([
         'REFRESH_CACHES',
         'REFRESH_PROJECTS'
      ]),
      create_project(){
        apiClient.post_create_project(this.name, this.description).then((response)=>{
           console.log(response.data)
           this.REFRESH_PROJECTS()
           this.dialog = false
        })
      }
    }
  }
</script>