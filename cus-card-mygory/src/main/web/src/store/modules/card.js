
export default {
    namespaced: true,
    state: {
        roleWid: '',
        categoryWid: '',
        config: {
            columns: 4,
            content: 1,
            rows: 2,
            showType: 1,
        },
        categoryItemInfo: [],
        tabInfo: [],
        serviceItemInfo: [],
    },
    mutations: {
        CHANGE_TAB_WID: (state, value) => {
            state.roleWid = value;
        },
        CHANGE_CATEGORY_WID: (state, value) => {
            state.categoryWid = value;
        },
        CHANGE_CONFIG: (state, value) => {
            state.tab = value;
        },
        CHANGE_CATEGORY_LIST: (state, value) => {
            state.categoryItemInfo = value;
        },
        CHANGE_TAB_LIST: (state, value) => {
            state.tabInfo = value;
        },
        CHANGE_ITEM_LIST: (state, value) => {
            state.serviceItemInfo = value;
        },
    },
    actions: {},
    getters: {
       
    }
};
