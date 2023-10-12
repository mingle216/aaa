<template>
  <svg
    width="16px"
    height="16px"
    viewBox="0 0 16 16"
    version="1.1"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink"
  >
    <defs>
      <rect id="path-1" x="0" y="0" width="16" height="16"></rect>
    </defs>
    <g
      stroke="none"
      stroke-width="1"
      fill="none"
      fill-rule="evenodd"
    >
      <g>
        <mask fill="white">
          <use xlink:href="#path-1"></use>
        </mask>
        <g></g>
        <g mask="url(#mask-2)" :fill="fillColor">
          <g transform="translate(0.000000, 1.000000)" id="路径">
            <path
              d="M14.5234269,1.36612259 C12.7289302,-0.397963519 9.83946627,-0.458798991 7.95370921,1.2140553 C6.06796641,-0.458798991 3.17850248,-0.397963519 1.38399151,1.36612259 C-0.440930079,3.19104418 -0.471340686,6.17175419 1.3535809,8.02708638 L1.41441637,8.08792185 L6.88918113,13.5626866 C7.46706821,14.1405737 8.40993961,14.1709986 9.01825156,13.5930972 L9.04866217,13.5626866 L14.5234269,8.08792185 C16.3483485,6.26300027 16.3787734,3.28229026 14.5842624,1.45736867 C14.5538518,1.39654746 14.5538518,1.39654746 14.5234269,1.36612259 Z"
            ></path>
          </g>
        </g>
      </g>
    </g>
  </svg>
</template>

<script>
export default {
	props: {
		fillColor: {
			type: String,
			default: '#BFBFBF'
		}
	}
}
</script>