<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="80rem"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          fab
          dark
          color="indigo"
          v-bind="attrs"
          v-on="on"
          small
        >
          <v-icon dark>
            mdi-plus
          </v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Add cache from JDBC
        </v-card-title>
        <v-card-text>
            <v-text-field
              label="Query"
              required
              v-model="body.query"
            ></v-text-field>
            <v-select
              :items="types"
              label="Type"
              v-model="body.type"
              required
            ></v-select>
            <v-text-field
              label="CacheName"
              v-model="body.cacheName"
              required
            ></v-text-field>
            <v-text-field
              label="JDBC URL"
              v-model="body.connectionProperties.url"
              required
            ></v-text-field>
            <v-text-field
              label="login"
              v-model="body.connectionProperties.login"
              required
            ></v-text-field>
            <v-text-field
              label="password"
              v-model="body.connectionProperties.password"
              required
            ></v-text-field>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="warning"
            text
            @click="dialog = false"
          >
            Cancel
          </v-btn>
          <v-btn
            color="primary"
            text
            @click="create_cache()"
          >
            Confirm
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
  import apiClient from '@/plugins/backendClient.js'
  import {mapActions} from 'vuex'
  import {mapGetters} from 'vuex'

  export default {
    params: {
        item: Object
    },
    data () {
      return {
        dialog: false,
        types: [
            "POSTGRESQL",
        ],
        body: {
          "query": "select * from bookings.aircrafts_data",
          "connectionProperties": {
            "url": "jdbc:postgresql://localhost:5432/demo?currentSchema=bookings",
            "login": "perfcona",
            "password": "perfcona"
          },
          "type": "POSTGRESQL",
          "cacheName": "aircraft",
          "projectId": "20c794f4-46d2-46a1-af19-5efbd1addf5d"
        }
      }
    },
    methods: {
      ...mapGetters([
        'GET_CURRENT_PROJECT'
      ]),
      ...mapActions([
         'REFRESH_CURRENT_PROJECT'
      ]),
      create_cache(){
        this.body.projectId = this.GET_CURRENT_PROJECT().id
        apiClient.post_jdbc_cache(this.body).then((response)=>{
           console.log(response.data)
           this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())
           this.dialog = false
        })
      }
    }
  }
</script>