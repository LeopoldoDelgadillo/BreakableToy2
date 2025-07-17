import { useState } from "react";

export function useMiddleConfig() {
  const [middleValue, setMiddleValue] = useState(0);

  function changeValue(value: number) {
    setMiddleValue(value);
  }

  return {
    middleValue,
    changeValue
  };
}