<template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="80rem"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          color="primary"
          outlined
          v-bind="attrs"
          v-on="on"
          class="ma-2 white--text"
        >
          Import to cache
          <v-icon
           right
           dark
          >
           mdi-database-import
          </v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="text-h5">
          Confirm import
        </v-card-title>
        <v-card-text>
        import file [ {{item.name}}] to cache
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
            @click="reload_request()"
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
        reload_request(){
            apiClient.post_reload_cache(this.item.project_id, this.item.name).then((response)=>{
                console.log(response.data)
                this.dialog = false
            })
        }
    }
  }
</script>