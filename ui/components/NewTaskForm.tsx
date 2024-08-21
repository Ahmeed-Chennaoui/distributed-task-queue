"use client";
import React, { useState } from "react";
import { priorities, Color } from "@/public/priorities";
import { TaskParams, taskTypes } from "@/public/taskTypes";
import backend from "@/api/backend";
import { taskTypesLogos } from "@/public/taskTypes";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Button,
  Input,
  SelectItem,
  Select,
  Selection,
  Chip,
  Image,
} from "@nextui-org/react";
import DurationInput from "./DurationInput";
import GenerateTaskParams from "./GenerateTaskParams";
import { Task } from "./TaskCard";
import { stateColors } from "@/public/states";
interface NewTaskFormProps {
  isOpen: boolean;
  onOpenChange: () => void;
  taskList: Task[] | undefined;
}

function NewTaskForm({ isOpen, onOpenChange, taskList }: NewTaskFormProps) {
  const [taskName, setTaskName] = useState("");
  const [TaskType, setTaskType] = useState("");
  const [duration, setDurarion] = useState(60);
  const [priority, setPriority] = useState(0);
  const [priorityColor, setPriorityColor] = useState<Color>("success");
  const [dependencies, setDependencies] = useState<string[] | undefined>([]);
  const [params, setParams] = useState<TaskParams<string | number | object>>(
    {}
  );

  const handlePriorityChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    if (!isNaN(Number(e.target.value))) {
      setPriority(Number(e.target.value));
      const newColor: Color = priorities
        .filter((priority) => priority.value === Number(e.target.value))
        .map((priority) => priority.color)[0];
      setPriorityColor(newColor);
    }
  };
  const handleTaskTypeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setTaskType(e.target.value);
    const newTaskParams: TaskParams<string | number | object> = taskTypes
      .filter((taskType) => taskType.value === e.target.value)
      .map((TaskType) => TaskType.paramsInterface)[0];
    setParams(newTaskParams);
  };
  const handleDependenciesSelectionChange = (keys: Selection) => {
    if (typeof keys === "string") {
      setDependencies([keys]);
    } else {
      const selectedArray = Array.from(keys as Set<string>);
      setDependencies(selectedArray);
    }
  };
  const handleSubmit = async () => {
    const requestBody = {
      task_name: taskName,
      task_type: TaskType,
      parameters: params,
      timeout: duration,
      dependencies: dependencies || [],
      priority: priority,
    };
    const response = await backend.post("/task", requestBody);
    setDependencies([]);
    console.log(response);
  };
  return (
    <Modal
      isOpen={isOpen}
      onOpenChange={onOpenChange}
      size="lg"
      placement="center"
      backdrop="blur"
      className="max-h-[90vh]"
      scrollBehavior="inside"
    >
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex flex-col gap-1 ">
              Create a new task
            </ModalHeader>
            <ModalBody>
              <Input
                isRequired
                value={taskName}
                autoFocus
                label="Task Name"
                variant="bordered"
                onValueChange={(newTaskName) => {
                  setTaskName(newTaskName);
                }}
              />
              <Select
                isRequired
                label="Task Type"
                variant="bordered"
                selectedKeys={[TaskType]}
                onChange={handleTaskTypeChange}
              >
                {taskTypes.map((taskType) => (
                  <SelectItem key={taskType.value} value={taskType.value}>
                    {taskType.label}
                  </SelectItem>
                ))}
              </Select>
              {taskList && (
                <Select
                  isMultiline
                  items={taskList}
                  variant="bordered"
                  onSelectionChange={handleDependenciesSelectionChange}
                  value={dependencies}
                  label="Dependencies"
                  selectionMode="multiple"
                  renderValue={(items) => {
                    return (
                      <div className="flex flex-wrap gap-2">
                        {items.map((item) => (
                          <Chip
                            color={
                              item?.data
                                ? stateColors[item.data?.state]
                                : "default"
                            }
                            variant="dot"
                            key={item.data?.task_id}
                          >
                            {item?.data?.task_name}
                          </Chip>
                        ))}
                      </div>
                    );
                  }}
                >
                  {(task) => (
                    <SelectItem key={task.task_id} value={task.task_id}>
                      <div className="flex gap-4 items-center">
                        <Image
                          className="rounded-none"
                          alt="task_icon"
                          src={taskTypesLogos[task.task_type]?.src}
                          height={32}
                          width={32}
                        />
                        <Chip
                          color={stateColors[task.state]}
                          variant="bordered"
                          className="text-tiny w-10"
                        >
                          <div className="w-[9ch] text-center">
                            {task.state}
                          </div>
                        </Chip>
                        <div>
                          <div className="w-[20ch] truncate text-ellipsis">
                            {task.task_name}
                          </div>
                          <div className="text-secondaryText text-tiny ">
                            {task.task_id}
                          </div>
                        </div>
                      </div>
                    </SelectItem>
                  )}
                </Select>
              )}
              <DurationInput setDuration={setDurarion} duration={duration} />
              <Select
                isRequired
                color={priorityColor}
                label="Task Priority"
                selectedKeys={[priority]}
                onChange={handlePriorityChange}
              >
                {priorities.map((priority) => (
                  <SelectItem
                    color={priority.color}
                    key={priority.value}
                    value={priority.value}
                  >
                    {priority.label}
                  </SelectItem>
                ))}
              </Select>
              {params && Object.keys(params).length > 0 && (
                <GenerateTaskParams setParams={setParams} params={params} />
              )}
            </ModalBody>
            <ModalFooter>
              <Button color="secondary" variant="bordered" onPress={onClose}>
                Cancel
              </Button>
              <Button
                color="primary"
                onPress={async () => {
                  await handleSubmit();
                  onClose();
                }}
              >
                Create
              </Button>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}
export default NewTaskForm;
