import { createVuetify } from 'vuetify'
import 'vuetify/styles'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'


import {
    VDataTable,
    VDataTableServer,
    VDataTableVirtual,
} from "vuetify/labs/VDataTable";

const vuetify = createVuetify({
    components: {
        VDataTable,
        VDataTableServer,
        VDataTableVirtual,
        ...components
    },
    directives,
})

export default vuetify