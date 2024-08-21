import React from 'react'
import { TaskParams } from '@/public/taskTypes'
import { Input, Accordion, Card, AccordionItem } from '@nextui-org/react'
interface GenerateTaskParamsParams /* most indicative name */ {
    params : TaskParams<string|number|object>
    setParams : any
}
function GenerateTaskParams({params,setParams}: GenerateTaskParamsParams) {
    const handleChange = (key: string, value: string | number | object) => {
    setParams({ ...params, [key]: value });
  };

  return (
    <div className='w-[100%] shadow-insider rounded-3xl p-6'>
       <div className='mb-2 '> Task Parameters</div>
        {Object.entries(params).map(([key,value])=>{ 
            if(!Array.isArray(value)) return(
                <Input key={key} type={typeof value === 'number' ? 'number' : 'text'} variant='bordered' className='mb-3' label={key} value={String(value)} onValueChange={(val)=>handleChange(key,val)} />
        )
        
        })}
    </div>
  )
}

export default GenerateTaskParams