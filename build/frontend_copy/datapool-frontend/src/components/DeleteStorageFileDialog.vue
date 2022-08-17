<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="80rem"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          color="error"
          outlined
          v-bind="attrs"
          v-on="on"
          class="ma-2 white--text"
        >
          Delete
          <v-icon
           right
           dark
          >
           mdi-delete
          </v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Confirm deletion
        </v-card-title>
        <v-card-text>
        Are you sure you want to delete?
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
            @click="delete_file(item)"
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
        ...mapActions([
           'REFRESH_CURRENT_PROJECT'
        ]),
        ...mapGetters([
          'GET_CURRENT_PROJECT'
        ]),
       delete_file(item){
          apiClient.delete_file(item.project_id, item.name)
          this.REFRESH_CURRENT_PROJECT(this.GET_CURRENT_PROJECT())
          this.dialog = false
       }
    }
  }
</script>