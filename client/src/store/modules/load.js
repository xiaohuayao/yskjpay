// export default (url) =>()=>import(`@/views/${url}.vue`)

export const loadView = (url) => {
    return (resolve) => require([`@/views/${url}.vue`], resolve)
  };