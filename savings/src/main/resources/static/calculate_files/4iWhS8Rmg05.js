;/*FB_PKG_DELIM*/

__d("BizKitScopingContext",["emptyFunction","react"],(function(a,b,c,d,e,f,g){"use strict";a=d("react");b=a.createContext({assets:[],globalScope:null,setAssets:c("emptyFunction"),setGlobalScope:c("emptyFunction")});g["default"]=b}),98);
__d("BusinessAssetTypeEnum.facebook",["$InternalEnum"],(function(a,b,c,d,e,f){a=b("$InternalEnum").Mirrored(["ADS_EVENT_SOURCE","ADVANCED_ANALYTICS_INSTANCE","AD_ACCOUNT","AD_ACCOUNT_CREATION_REQUEST","AD_DRAFT_WORKSPACE","AD_IMAGE","AD_STUDY","APP","BANK","BLOCK_LIST","BRAND","BUSINESS_CREATIVE_ASSET","BUSINESS_CREATIVE_FOLDER","BUSINESS_FRANCHISE_CONFIG","BUSINESS_IMAGE","BUSINESS_LOCATIONS_WRAPPER","BUSINESS_LOYALTY_PROGRAM","BUSINESS_PAYOUT_ACCOUNT","BUSINESS_REQUEST","BUSINESS_RESOURCE_GROUP","BUSINESS_UNIT","BUSINESS_VIDEO","CAIPT_ASSET","CLOUD_PLAYABLE_ASSET","CREATOR_MARKETPLACE_BRAND_PROFILE","CREATOR_SELLER_PROFILE","CREDENTIAL_SHARE_REQUEST","CREDIT_CARD","CREDIT_PARTITION","CREDIT_PARTITION_CONFIG","CUSTOM_AUDIENCE","CUSTOM_CONVERSION","DYNAMIC_CONTENT_SET","EVENTS_DATASET","EVENTS_DATASET_NEW","EVENT_SOURCE_GROUP","EXAMPLE_CAT","FINANCE","FUNDING_SOURCE","GRP_PLAN","HOTEL_PRICE_FETCHER_PULL_CONFIG","INSTAGRAM_ACCOUNT","INSTAGRAM_ACCOUNT_V2","INSTAGRAM_BUSINESS_ASSET","IP","LEADS_ACCESS","LEGACY_LOGIN","LEGAL_ENTITY","MESSAGING_DATASET","MONETIZATION_PROPERTY","NEWS_PAGE","OFFLINE_CONVERSION_DATA_SET","OFFSITE_EMAIL_ACCOUNT","OWNED_DOMAIN","PAGE","PAGE_FOR_LOCATIONS","PAYOUT_ACCOUNT","PHOTO","PIXEL","PLACE_PAGE_SET","PLATFORM_CUSTOM_AUDIENCE","PRODUCT_CATALOG","PROFILE_PLUS","PROJECT","PUBLISHER_WHITE_LIST","RECEIPT","REGISTERED_TRADEMARK","RESELLER_VETTING_OE_REQUEST","SAVED_AUDIENCE","SELLER_PROFILE","SIGNALS_EVENT_NAME","SIGNAL_SEGMENT","SLICED_EVENT_SOURCE_GROUP","SPACO_DS_DATA_COLLECTION","SYSTEM_USER","UNKNOWN","USER","VIDEO_ASSET","WHATSAPP_BUSINESS_ACCOUNT"]);c=a;f["default"]=c}),66);
__d("getAssetsByType",[],(function(a,b,c,d,e,f){"use strict";function a(a){return{adAccount:a.filter(function(a){return a.type==="AD_ACCOUNT"}),appAccount:a.filter(function(a){return a.type==="APP"}),igAccount:a.filter(function(a){return a.type==="INSTAGRAM_ACCOUNT_V2"}),pageAccount:a.filter(function(a){return a.type==="PAGE"}),wabAccount:a.filter(function(a){return a.type==="WHATSAPP_BUSINESS_ACCOUNT"})}}f["default"]=a}),66);
__d("getFirstAssetOfType",[],(function(a,b,c,d,e,f){"use strict";function a(a,b){return a.find(function(a){return a.type===b})}f["default"]=a}),66);
__d("useBizKitSelectedAssets",["BizKitScopingContext","getAssetsByType","getFirstAssetOfType","react"],(function(a,b,c,d,e,f,g){"use strict";var h=d("react").useContext;function a(){var a=h(c("BizKitScopingContext")),b=a.assets,d=a.setAssets;a=(a=(a=c("getFirstAssetOfType")(b,"PAGE"))!=null?a:b[0])!=null?a:null;return{assetID:a==null?void 0:a.id,assets:b,assetsByType:c("getAssetsByType")(b),assetType:a==null?void 0:a.type,setAssets:d,setSelectedLinkedAsset:function(a){d(a.assets)}}}g["default"]=a}),98);
__d("personalGlobalScope",["CurrentUser"],(function(a,b,c,d,e,f,g){"use strict";a={id:c("CurrentUser").getAccountID(),type:"PERSONAL"};g.personalGlobalScope=a}),98);
__d("useGlobalScope",["BizKitScopingContext","personalGlobalScope","react"],(function(a,b,c,d,e,f,g){"use strict";var h=d("react").useContext;function a(){var a=h(c("BizKitScopingContext"));a=a.globalScope;return(a=a)!=null?a:d("personalGlobalScope").personalGlobalScope}g["default"]=a}),98);
__d("getBusinessAssetList",[],(function(a,b,c,d,e,f){"use strict";function a(a,b){return a.map(function(a){return{business_account_id:b==null?void 0:b.id,business_asset_id:a.id}})}f["default"]=a}),66);
__d("useBusinessAssets",["getBusinessAssetList","react","useBizKitSelectedAssets","useGlobalScope"],(function(a,b,c,d,e,f,g){"use strict";var h=d("react").useMemo;function a(a){var b=c("useBizKitSelectedAssets")();b=b.assets;var d=a==null?b:a,e=c("useGlobalScope")();return h(function(){return c("getBusinessAssetList")(d,e)},[e,d])}g["default"]=a}),98);
__d("VideoDisplayTimePlayButton",["CSS","DataStore","Event"],(function(a,b,c,d,e,f,g){"use strict";var h={},i="_spinner";function a(a){return d("DataStore").get(a,"clicked",!1)}function b(a,b){var e=a.id;h[e]=c("Event").listen(a,"click",function(){b&&(d("CSS").hide(a),d("CSS").show(b)),d("DataStore").set(a,"clicked",!0)});b&&(h[e+i]=c("Event").listen(b,"click",function(){b&&d("CSS").hide(b),d("CSS").show(a),d("DataStore").set(a,"clicked",!1)}))}function e(a){a=a.id;Object.prototype.hasOwnProperty.call(h,a)&&h[a].remove();a=a+i;Object.prototype.hasOwnProperty.call(h,a)&&h[a].remove()}g.getClicked=a;g.register=b;g.unregister=e}),98);
__d("VideosRenderingInstrumentation",["DataStore","VideoPlayerHTML5Experiments","performanceAbsoluteNow"],(function(a,b,c,d,e,f,g){function h(a){var b=c("VideoPlayerHTML5Experiments").useMonotonicallyIncreasingTimers?c("performanceAbsoluteNow")():Date.now();d("DataStore").set(a,"videos_rendering_instrumentation",b);return b}function a(a){var b=d("DataStore").get(a,"videos_rendering_instrumentation",NaN);Number.isNaN(b)&&(b=h(a));return b}g.storeRenderTime=h;g.retrieveRenderTime=a}),98);