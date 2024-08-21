import React, { useState, useEffect } from "react";
import { Input } from "@nextui-org/react";
interface DurationInputParams {
  setDuration: (duration: number) => void;
  duration: number;
}
function DurationInput({ setDuration, duration }: DurationInputParams) {
  const [hours, setHours] = useState("0");
  const [minutes, setMinutes] = useState("1");
  const [seconds, setSeconds] = useState("0");
  useEffect(() => {
    updateDuration();
  }, [hours, minutes, seconds]);
  const handleChange = async (
    e: React.ChangeEvent<HTMLInputElement>,
    setter: (s: string) => void
  ) => {
    const value = e.target.value;
    if (value !== "" && !isNaN(Number(value))) {
      const x = Math.min(parseInt(value), 59);
      setter(x.toString());
    }
    if (value === "") await setter("");
  };
  const updateDuration = () => {
    setDuration(Number(seconds) + Number(minutes) * 60 + Number(hours) * 3600);
  };

  return (
    <div className="mb-2">
      <label className="text-secondaryText">Time Limit</label>
      <div className="flex gap-2 items-center justify-between">
        <Input
          isInvalid={duration <= 0}
          type="number"
          label="Hours"
          value={hours}
          onValueChange={(e) => {
            setHours(e);
          }}
          maxLength={2}
          variant="bordered"
          className="w-[30%]"
        />
        <Input
          isInvalid={duration <= 0}
          pattern="\d*"
          label="Minutes"
          onChange={async (e) => {
            await handleChange(e, setMinutes);
          }}
          value={minutes}
          maxLength={2}
          variant="bordered"
          className="w-[30%]"
        />
        <Input
          isInvalid={duration <= 0}
          pattern="\d*"
          label="seconds"
          value={seconds}
          onChange={async (e) => {
            await handleChange(e, setSeconds);
          }}
          variant="bordered"
          className="w-[30%]"
        />
      </div>
      {duration <= 0 && (
        <div className="text-danger text-center mt-1">
          Please enter a valid duration
        </div>
      )}
    </div>
  );
}

export default DurationInput;
