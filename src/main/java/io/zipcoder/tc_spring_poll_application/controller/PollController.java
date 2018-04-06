package io.zipcoder.tc_spring_poll_application.controller;

import io.zipcoder.tc_spring_poll_application.domain.Poll;
import io.zipcoder.tc_spring_poll_application.exception.ResourceNotFoundException;
import io.zipcoder.tc_spring_poll_application.repositories.PollRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class PollController {
  @Inject
  private PollRepository pollRepository;

  @RequestMapping(value ="/polls", method= RequestMethod.GET)
  public ResponseEntity<Iterable<Poll>> getAllPolls(){
    Iterable<Poll> allPolls = pollRepository.findAll();
    return new ResponseEntity<>(allPolls, HttpStatus.OK);
  }

  @RequestMapping(value ="/polls", method= RequestMethod.POST)
  public ResponseEntity<?> createPoll(@Valid @RequestBody Poll poll){
    poll = pollRepository.save(poll);

    //Set the location header for the newly created resource
    HttpHeaders responseHeaders = new HttpHeaders();

    URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(poll.getId()).toUri();
    responseHeaders.setLocation(newPollUri);
    return new ResponseEntity<>(new HttpHeaders(), HttpStatus.CREATED);
  }
  public void verifyPoll(Long pollId) throws ResourceNotFoundException {
    Poll poll = pollRepository.findOne(pollId);
    if(poll == null) {
      throw new ResourceNotFoundException("Poll with id " + pollId + " not found");
    }
  }
  @RequestMapping(value="/polls/{pollId}", method= RequestMethod.GET)
  public ResponseEntity<?> getPoll(@PathVariable Long pollId){
    Poll poll = pollRepository.findOne(pollId);
    return new ResponseEntity<>(poll, HttpStatus.OK);
  }
  @RequestMapping(value="/polls/{pollId}", method= RequestMethod.PUT)
  public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId){
    Poll p = pollRepository.save(poll);
    return new ResponseEntity<>(HttpStatus.OK);
  }
  @RequestMapping(value="/polls/{pollId}", method= RequestMethod.DELETE)
  public ResponseEntity<?> deletePoll(@PathVariable Long pollId){
    pollRepository.delete(pollId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
