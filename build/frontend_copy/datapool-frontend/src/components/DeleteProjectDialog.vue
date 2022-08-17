<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="80rem"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-icon
          medium
          v-bind="attrs"
          v-on="on"
        >
          mdi-delete
        </v-icon>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Confirm deletion
        </v-card-title>
        <v-card-text>
        Are you sure you want to delete cache - {{this.item.cacheName}}
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click="dialog = false"
          >
            Cancel
          </v-btn>
          <v-btn
            color="error"
            text
            @click="delete_action()"
          >
            Confirm
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>


<script>
  import {mapActions} from 'vuex'
  import {mapGetters} from 'vuex'
  import apiClient from '@/plugins/backendClient.js'

  export default {
    props: {
      item: {
        type:Object,
        default(){
            return {}
        }
      }
    },
    data () {
      return {
        dialog: false,
      }
    },
    methods: {
        ...mapGetters([
            'GET_CURRENT_PROJECT'
        ]),
        ...mapActions([
           'REFRESH_CACHES',
           'REFRESH_CURRENT_PROJECT'
        ]),
        delete_action(){
            console.log(this.item)
            apiClient.delete_project_request(this.GET_CURRENT_PROJECT().id)
            this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())
            this.dialog = false
        }
    }
  }
</script>