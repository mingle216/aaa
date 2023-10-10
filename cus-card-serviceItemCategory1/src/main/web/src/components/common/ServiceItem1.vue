<template>
    <div ref="serviceItem" v-if="dataItem && config" class="ser-item" @click="openItemDetail(dataItem)">
        <div>
            <img v-if="dataItem.iconLink" :src="dataItem.iconLink">
            <img v-else src="../../assets/error.png">
        </div>
        <div class="name" :title="dataItem.itemName">{{dataItem.itemName}}
            <!-- <p class="name" :title="dataItem.itemName">{{dataItem.itemName}}</p> -->
            <!-- <p class="bm" :title="dataItem.subjectName">{{dataItem.subjectName}}</p> -->
        </div>


        <div class="item-title dataItem" style="display:none">
            <div class="item-icon" v-if="isShowIcon">
                <template v-if="isDept">
                    <template v-if="config.departClassIcon === 1">
                        <i class="item-icon-font" :class="
                  dataItem.iconLink ? dataItem.iconLink : 'iconfont icon-default'
                "></i>
                    </template>
                    <template v-else>
                        <we-image class="item-icon" :src="dataItem.picLink ? dataItem.picLink : defaultImg">
                            <div slot="error">
                                <img :src="defaultImg" />
                            </div>
                        </we-image>
                    </template>
                </template>
                <template v-else>
                    <template v-if="dataItem.iconLink">
                        <i class="item-icon-font" :class="dataItem.iconLink"></i>
                    </template>
                    <template v-else>
                        <we-image class="item-icon" :src="dataItem.picLink ? dataItem.picLink : defaultImg">
                            <div slot="error">
                                <img :src="defaultImg" />
                            </div>
                        </we-image>
                    </template>
                </template>
            </div>
            <!-- <div v-else class="item-none-icon">
            <div class="none-icon portal-primary-backgroundcolor-lv1"></div>
        </div> -->
            <div class="title portal-font-color-lv1">
                {{ dataItem.subjectName }}
            </div>
        </div>
        <div class="item-list portal-font-color-lv2" style="display:none">
            <div class="item-con">
                <template v-if="config.content === 1">
                    <template v-for="(item, index) in secondCategory">
                        <div class="item-item" :key="index" v-if="index < 3">
                            {{ item }}
                        </div>
                    </template>
                </template>
                <template v-else-if="config.content === 2">
                    <div>{{ dataItem.itemCount }}项服务</div>
                    <div>{{ dataItem.onlineDealItemCount }}项可在线办理</div>
                </template>
                <template v-else>
                    <template v-for="(item, index) in dataItem.itemList">
                        <div class="item-item" :key="index" v-if="index < 3" @click.stop="openItemDetail(item)">
                            {{ item.itemName }}
                        </div>
                    </template>
                </template>
            </div>
            <div class="arrow-con">
                <div class="arrow">
                    <i class="iconfont icon-shixiangfenleijiantou"></i>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        props: {
            dataItem: {
                type: Object,
                default: () => {
                    return {
                        secondCategory: "",
                        subjectName: "",
                        picLink: "",
                        iconLink: "",
                        itemList: [],
                    };
                },
            },
            isDept: {
                type: Boolean,
                default: true,
            },
            config: {
                type: Object,
                default: () => {
                    return {
                        columns: 1,
                        rows: 2,
                        content: 2,
                        departClassIcon: 0, //部门分类下是否显示图标，0：隐藏，1：字体，2：png
                        otherClassIcon: 0, //其他分类是否显示图标，0：隐藏，1：显示
                    };
                },
            },
        },

        computed: {
            isShowIcon() {
                //是否显示图标
                if (
                    (this.isDept && this.config.departClassIcon === 0) ||
                    (!this.isDept && this.config.otherClassIcon === 0)
                ) {
                    return false;
                }
                return true;
            },
            secondCategory() {
                if (this.dataItem?.secondCategory) {
                    let arr = this.dataItem?.secondCategory.split(" | ") || [];
                    return arr.map((item) => {
                        return item.trim();
                    });
                }
                return [];
            },
        },
        mounted() {
            // console.log(this.dataItem);
        },
        data() {
            return {
                defaultImg: require("../../assets/default.svg"),
            };
        },
        methods: {
            clickItem() {
                this.$emit("clickItem", this.dataItem);
            },
            openItemDetail(item) {
                console.log(item)
                if(item.itemWid){
                    let href = 'https://espace.cuc.edu.cn/zsfa3/index.html#/itemDetail?wid='+item.itemWid
                window.open(href)
                }else{
window.shell.openServiceItem({
                    ...item,
                    wid: item.itemWid,
                    name: item.itemName
                })
                }
                
                
            }
        },
    };
</script>

<style lang="less" scoped>
    .name {
        font-weight: 400;
        font-size: 15px;
        line-height: 18px;
        color: #262626;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
    }

    .bm {
        font-weight: 400;
        font-size: 14px;
        line-height: 22px;
        margin-top: 2px;
        color: #8C8C8C;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .ser-item>div:first-child {
        height: 36px;
        width: 36px;
        margin-right: 5px;
    }

    .ser-item>div:nth-child(2) {
        width: calc(100% - 24px - 5px - 12px);
    }

    .ser-item>div>img {
        height: 36px;
        width: 36px;
    }

    .ser-item:hover {
        background: rgba(164, 0, 0, 0.06);

    }

    .ser-item:hover>div>.name {
        font-weight: 700;
        color: #A40000;
    }

    .ser-item {
        cursor: pointer;
        /* border: 2px dotted #f0f0f0; */
        /* height: 116px; */
        box-sizing: border-box;
        border-radius: 4px;
        /* margin-bottom: 20px; */
        padding: 12px;
        width: 20%;
        display: flex;
        align-items: center;


        &:hover {
            border-color: transparent !important;
        }

        .item-title {
            height: 32px;
            line-height: 32px;
            display: flex;
            margin-bottom: 12px;

            .item-icon {
                height: 32px;
                width: 32px;

                .iconfont {
                    font-size: 32px;
                }

                .item-icon {
                    width: 32px;
                    height: 32px;
                    object-fit: contain;
                }
            }

            // .item-none-icon{
            //   height: 22px;
            //   width: 4px;
            //   position: relative;
            //   .none-icon{
            //     height: 12px;
            //     width: 4px;
            //     position:absolute;
            //     top:50%;
            //     border-radius: 2px;
            //     margin-top:-6px;
            //   }
            // }
            .title {
                padding: 0 8px;
                flex: 1;
                width: 0;
                height: 100%;
                font-size: 16px;
                font-weight: bold;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }
        }

        .item-list {
            display: flex;
            height: 82px;

            .item-item {
                font-size: 14px;
                height: 22px;
                line-height: 22px;
                margin-bottom: 8px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }

            .item-con {
                flex: 1;
                width: 0;
            }

            .arrow-con {
                position: relative;
                width: 56px;

                .arrow {
                    width: 38px;
                    height: 38px;
                    line-height: 38px;
                    text-align: center;
                    border-radius: 4px;
                    position: absolute;
                    right: 0;
                    bottom: 0;
                    background-color: #f5f5f5;
                    color: #bcbcbc;
                }
            }
        }
    }
</style>