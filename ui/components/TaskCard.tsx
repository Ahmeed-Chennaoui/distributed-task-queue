"use client";
import { priorities } from "@/public/priorities";
import { State, stateColors } from "@/public/states";
import { taskTypesLogos } from "@/public/taskTypes";
import {
  Card,
  CircularProgress,
  Chip,
  Image,
  Tabs,
  Tab,
  Snippet,
  CardBody,
} from "@nextui-org/react";

import React, { useEffect, useState } from "react";

export interface Task {
  priority: number;
  task_id: string;
  attempts: number;
  created_at: number;
  dependencies: string[];
  state: State;
  task_name: string;
  task_type: string;
  timeout: number;
  result: String;
}
interface TaskCardProps {
  task: Task;
  taskList: Task[] | undefined;
}
export function TaskCard({ task, taskList }: TaskCardProps) {
  const [progress, setProgress] = useState(0);
  const [showResult, setShowResult] = useState(false);
  const [selected, setSelected] = useState("Dependencies");
  const handleClick = () => {
    console.log(task.result);
    if (task.state == "Completed") {
      setShowResult(!showResult);
    }
  };
  const image = taskTypesLogos[task.task_type]?.src;
  useEffect(() => {
    if (task.state === "Processing") {
      for (let i = 1; i <= task.timeout; i++) {
        setTimeout(() => {
          setProgress(i);
        }, 1000 * i);
      }
    }
    if (task.state === "Completed" && task.result) setSelected("Result");
  }, [task.state]);
  return (
    <Card
      className={
        "w-[23%] h-56 px-3 py-3 flex flex-col justify-between "
        // +(task.state == "Completed" && task.result != ""
        //   ? " cursor-pointer hover:scale-105 hover:z-2"
        //   : " ")
      }
      // isPressable={task.result != ""}
      // disableRipple
      // onClick={() => handleClick()}
    >
      <header className="flex justify-between w-full text-start">
        <div className="flex gap-2">
          <Image
            className="rounded-none"
            alt="task_icon"
            src={image}
            height={32}
            width={32}
          />
          <div>
            <div className="text-md max-w-[28ch] truncate hover:w-auto hover:text-wrap">
              {task.task_name}
            </div>

            <div className="text-tiny text-secondaryText">{task.task_id}</div>
          </div>
        </div>
        {task.state === "Processing" && (
          <CircularProgress value={progress} maxValue={task.timeout} />
        )}
      </header>
      <div className="flex w-full flex-col justify-between h-[55%]">
        <Tabs
          disabledKeys={task.result ? [] : ["Result"]}
          aria-label="Disabled Options"
          size="sm"
          onSelectionChange={(key)=>{setSelected(key.toString())}}
          selectedKey={selected}
        >
          <Tab key="Dependencies" title="Dependencies">
            <div
              className={
                "flex gap-x-1 gap-y-[1px] items-center flex-wrap border-gray border p-1 rounded-xl text-tiny  mb-2 min-h-10"
              }
            >
              {taskList &&
              taskList.filter((t) => task.dependencies.includes(t.task_id))
                .length > 0 ? (
                taskList
                  .filter((t) => task.dependencies.includes(t.task_id))
                  .map((task) => (
                    <Chip
                      color={stateColors[task.state]}
                      variant="dot"
                      key={task.task_id}
                    >
                      {task.task_name}
                    </Chip>
                  ))
              ) : (
                <div className="text-sm">Task has no dependencies</div>
              )}
            </div>
          </Tab>
          <Tab key="Result" title="Result">
            <div className="border-gray border p-1 rounded-xl text-tiny  mb-2 min-h-10 items-center flex h-12">
              <Snippet hideSymbol className="w-full bg-transparent">
                {task.result.replace(/^"(.*)"$/, '$1')}
              </Snippet>
            </div>
          </Tab>
        </Tabs>
      </div>
      {/* <div className={"text-start w-full"}>
        <div className="text-tiny ">
          {showResult ? "Result:" : "Dependencies:"}
        </div>
        <main className="border-gray border p-1 rounded-xl text-tiny  mb-2 min-h-10 items-center flex">
          <div
            className={
              "flex gap-x-1 items-center flex-wrap " +
              (showResult ? "hidden" : "")
            }
          >
            {taskList &&
            taskList.filter((t) => task.dependencies.includes(t.task_id))
              .length > 0 ? (
              taskList
                .filter((t) => task.dependencies.includes(t.task_id))
                .map((task) => (
                  <Chip
                    color={stateColors[task.state]}
                    variant="dot"
                    key={task.task_id}
                  >
                    {task.task_name}
                  </Chip>
                ))
            ) : (
              <div className="text-sm">Task has no dependencies</div>
            )}
          </div>
          {showResult && (
            <Snippet hideSymbol className="w-full bg-transparent">
              {task.result}
            </Snippet>
          )}
        </main>
      </div> */}
      <footer className="flex-row-reverse flex justify-between w-full">
        <Chip color={stateColors[task.state]} variant="bordered">
          {task.state}
        </Chip>
        <Chip
          color={
            priorities
              .filter((priority) => priority.value === task.priority)
              .map((priority) => priority.color)[0]
          }
        >
          {priorities
            .filter((priority) => priority.value === task.priority)
            .map((priority) => priority.label)[0] + " Priority"}
        </Chip>
      </footer>
    </Card>
  );
}
