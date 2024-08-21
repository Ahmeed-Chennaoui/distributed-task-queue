"use client";
import React from "react";
import { Card } from "@nextui-org/react";
import { FaPlus } from "react-icons/fa";
import NewTaskForm from "@/components/NewTaskForm";
import { useDisclosure } from "@nextui-org/react";
import {Task} from "@/components/TaskCard"

interface NewTaskParams {
  taskList : Task[] | undefined
}
function NewTask({taskList}: NewTaskParams) {
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  return (
    <>
      <Card
        isPressable
        disableRipple
        onPress={onOpen}
        // className=" w-[24%] flex-col items-center justify-center border-dashed  cursor-pointer hover:scale-90"
        className="flex gap-2 items-center flex-row p-3"
      >
        <FaPlus size={32} className="fill-secondary" onClick={onOpen} />
        <div>Add a Task</div>
      </Card>
      <NewTaskForm isOpen={isOpen} onOpenChange={onOpenChange} taskList={taskList}/>
    </>
  );
}

export default NewTask;
