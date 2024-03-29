import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INote, Note } from '../note.model';

import { NoteService } from './note.service';

describe('Note Service', () => {
  let service: NoteService;
  let httpMock: HttpTestingController;
  let elemDefault: INote;
  let expectedResult: INote | INote[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NoteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      qualification: 'AAAAAAA',
      period: 'AAAAAAA',
      startDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Note', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Note()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Note', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          qualification: 'BBBBBB',
          period: 'BBBBBB',
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Note', () => {
      const patchObject = Object.assign({}, new Note());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Note', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          qualification: 'BBBBBB',
          period: 'BBBBBB',
          startDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Note', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNoteToCollectionIfMissing', () => {
      it('should add a Note to an empty array', () => {
        const note: INote = { id: 123 };
        expectedResult = service.addNoteToCollectionIfMissing([], note);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(note);
      });

      it('should not add a Note to an array that contains it', () => {
        const note: INote = { id: 123 };
        const noteCollection: INote[] = [
          {
            ...note,
          },
          { id: 456 },
        ];
        expectedResult = service.addNoteToCollectionIfMissing(noteCollection, note);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Note to an array that doesn't contain it", () => {
        const note: INote = { id: 123 };
        const noteCollection: INote[] = [{ id: 456 }];
        expectedResult = service.addNoteToCollectionIfMissing(noteCollection, note);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(note);
      });

      it('should add only unique Note to an array', () => {
        const noteArray: INote[] = [{ id: 123 }, { id: 456 }, { id: 96092 }];
        const noteCollection: INote[] = [{ id: 123 }];
        expectedResult = service.addNoteToCollectionIfMissing(noteCollection, ...noteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const note: INote = { id: 123 };
        const note2: INote = { id: 456 };
        expectedResult = service.addNoteToCollectionIfMissing([], note, note2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(note);
        expect(expectedResult).toContain(note2);
      });

      it('should accept null and undefined values', () => {
        const note: INote = { id: 123 };
        expectedResult = service.addNoteToCollectionIfMissing([], null, note, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(note);
      });

      it('should return initial array if no Note is added', () => {
        const noteCollection: INote[] = [{ id: 123 }];
        expectedResult = service.addNoteToCollectionIfMissing(noteCollection, undefined, null);
        expect(expectedResult).toEqual(noteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
