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
          Generate remote token
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Remote token for HttpPool
        </v-card-title>
        <v-card-text>
            <v-textarea
              solo
              name="input-7-4"
              label="remote token"
              v-model="token"
            ></v-textarea>
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click="generate()"
          >
            generate
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>


<script>
  import apiClient from '@/plugins/backendClient.js'

  export default {
    data () {
      return {
        dialog: false,
        token: ''
      }
    },
    methods: {
        generate(){
            apiClient.remote_auth().then((response)=>{
               this.token = response.data.token
            })
        }
    }
  }
</script>